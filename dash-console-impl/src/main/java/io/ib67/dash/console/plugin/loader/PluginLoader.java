/*
 * MIT License
 *
 * Copyright (c) 2023 Kalculos and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.ib67.dash.console.plugin.loader;

import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import io.ib67.dash.console.IConsole;
import io.ib67.dash.console.plugin.IPluginManager;
import io.ib67.dash.console.plugin.PluginClassLoader;
import io.ib67.dash.console.plugin.exception.InvalidPluginInfoException;
import io.ib67.dash.console.plugin.java.SharedClassContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * A plugin loader accepts multiple path to plugin and tries its best to load them all.
 */
@Slf4j
class PluginLoader {
    private static final Pattern PLUGIN_NAME = Pattern.compile("^[a-zA-Z0-9_+]+");
    /**
     * Associated PluginManager.
     */
    protected final IPluginManager pluginManager;
    /**
     * Console used to inject
     */
    protected final IConsole console;
    protected final SharedClassContext sharedClassContext;

    protected PluginLoader(IPluginManager pluginManager, IConsole console, SharedClassContext sharedClassContext) {
        requireNonNull(this.sharedClassContext = sharedClassContext);
        requireNonNull(this.pluginManager = pluginManager);
        requireNonNull(this.console = console);
    }

    // ------- STATES ---------
    protected Map<String, TempPluginHolder> dependencyNodes;
    protected Set<String> presentPlugins;
    protected MutableNetwork<String, DependencyType> depNetwork = NetworkBuilder.directed().allowsSelfLoops(false).build();

    public void load(List<Path> pluginPath) {
        dependencyNodes = new HashMap<>(pluginManager.getPlugins().size() + pluginPath.size());
        presentPlugins = pluginManager.getPlugins().stream().map(it -> it.right.getInfo().name()).collect(Collectors.toSet());
        // try to load their information
        for (Path path : pluginPath) {
            if (path.getFileName().endsWith(".jar")) {
                loadJarPlugin(path); // this populates dependency nodes
            }
        }

        /* BUILD GRAPH */
        buildDependencyGraph();

        /** FIND MISSING NODES **/
        invalidateMissingNodes();

        /** TRY TO LOAD PLUGINS **/
        initializePlugins();

    }

    protected void initializePlugins() {
        // 这部分逻辑被拆分到 PluginInitializer.
        throw new UnsupportedOperationException();
    }

    private void invalidateMissingNodes() {
        boolean hasMissing;
        do {
            var missingDependencies = dependencyNodes.entrySet().stream()
                    .filter(it -> it.getValue().dependencyNodeState == DependencyNodeState.UNDISCOVERED)
                    .map(Map.Entry::getKey).toList();
            hasMissing = !missingDependencies.isEmpty();
            for (String missingDependency : missingDependencies) {
                dependencyNodes.remove(missingDependency);
                var successors = depNetwork.successors(missingDependency);
                for (String successor : successors) {
                    var edge = depNetwork.edgeConnectingOrNull(missingDependency, successor);
                    if (edge == DependencyType.DEPEND) {
                        log.warn("Cannot load plugin {} because {} is not found or cannot be loaded.", successor, missingDependency);
                        dependencyNodes.get(successor).dependencyNodeState = DependencyNodeState.UNDISCOVERED; // this will trigger a loop.
                    } else if (edge == null) {
                        log.error("Impossible null: Cannot find an edge in a closed graph.");
                    }
                }
                depNetwork.removeNode(missingDependency);
            }
        }
        while (hasMissing);
    }

    private void buildDependencyGraph() {
        var resultMap = new HashMap<String, DependencyNodeState>(dependencyNodes.size());
        for (Map.Entry<String, TempPluginHolder> entry : dependencyNodes.entrySet()) {
            var info = entry.getValue().classLoader.getPluginInfo();
            resultMap.put(info.name(), DependencyNodeState.DISCOVERED);
            // dependency is predecessor to the dependent plugin.
            // dependency <- current plugin
            for (String dependency : info.dependencies()) {
                resultMap.putIfAbsent(dependency, DependencyNodeState.UNDISCOVERED);
                depNetwork.addEdge(dependency, info.name(), DependencyType.DEPEND);
            }
            for (String dependency : info.loadAfters()) {
                resultMap.putIfAbsent(dependency, DependencyNodeState.UNDISCOVERED);
                depNetwork.addEdge(dependency, info.name(), DependencyType.SOFTDEPEND);
            }
        }
        resultMap.forEach((k, v) -> dependencyNodes.get(k).dependencyNodeState = v);
        var exist = new TempPluginHolder(DependencyNodeState.DISCOVERED, null);
        presentPlugins.forEach(s -> dependencyNodes.put(s, exist)); // mark loaded plugins.
    }

    private void loadJarPlugin(Path path) {
        try {
            var cl = new PluginClassLoader(path, pluginManager.getClass().getClassLoader(), sharedClassContext);
            // check for info
            var info = cl.getPluginInfo();
            if (!PLUGIN_NAME.matcher(info.name()).matches()) {
                throw new InvalidPluginInfoException("Illegal plugin name. Must be " + PLUGIN_NAME.pattern(), path);
            }
            if (presentPlugins.contains(info.name()) || dependencyNodes.containsKey(info.name())) {
                throw new InvalidPluginInfoException("Plugin with name \"" + info.name() + "\" is already loaded!", path);
            }

            var deps = new ArrayList<String>(info.dependencies().size() + info.loadAfters().size());
            deps.addAll(info.dependencies());
            deps.addAll(info.loadAfters());
            for (String dependency : deps) {
                if (!PLUGIN_NAME.matcher(dependency).matches())
                    throw new InvalidPluginInfoException("Illegal dependency name \"" + dependency + "\". Must be " + PLUGIN_NAME.pattern(), path);
                if (dependency.equals(info.name())) {
                    throw new InvalidPluginInfoException("Plugin \"" + dependency + "\" attempts to depend on itself!", path);
                }
            }
            dependencyNodes.put(info.name(), new TempPluginHolder(DependencyNodeState.DISCOVERED, cl));
        } catch (InvalidPluginInfoException e) {
            log.warn("Cannot load java plugin {}: {}", path.getFileName(), e);
        }
    }

    @AllArgsConstructor
    protected static class TempPluginHolder {
        DependencyNodeState dependencyNodeState;
        PluginClassLoader classLoader;
    }
}
