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

package io.ib67.dash.console.plugin;

import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import io.ib67.dash.console.plugin.exception.InvalidPluginInfoException;
import io.ib67.dash.console.plugin.exception.PluginException;
import io.ib67.dash.console.plugin.java.SharedClassContext;
import io.ib67.dash.console.plugin.loader.DependencyNodeState;
import io.ib67.dash.console.plugin.loader.DependencyType;
import io.ib67.dash.console.plugin.loader.OrderedGraphWalker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

/**
 * An utility class to load multiple plugins at the same time.
 */

@SuppressWarnings("UnstableApiUsage")
@RequiredArgsConstructor
@Slf4j(topic = "Plugin Loader")
public class BatchPluginLoader {
    private static final Pattern PLUGIN_NAME = Pattern.compile("^[a-zA-Z0-9_+]+");
    private final MutableNetwork<String, DependencyType> parent;
    private final MutableNetwork<String, DependencyType> current = NetworkBuilder.directed().build();
    private final SimplePluginManager pluginManager;
    private final SharedClassContext context;
    private Map<String, PluginClassLoader> infoMap;
    private HashMap<String, DependencyNodeState> loadStates;

    public void load(List<Path> plugins) {
        // try to load them all.
        infoMap = new HashMap<>(plugins.size());
        for (Path plugin : plugins) {
            if (plugin.endsWith("jar")) {
                initJarPlugin(plugin); // this does populate the infos map
            }
        }

        // build the graph
        loadStates = new HashMap<>(infoMap.keySet().size() + parent.nodes().size());
        parent.nodes().forEach(it -> loadStates.put(it, DependencyNodeState.DISCOVERED)); // already loaded and ready to use.

        for (Map.Entry<String, PluginClassLoader> entry : infoMap.entrySet()) {
            var name = entry.getKey();
            var info = entry.getValue().getPluginInfo();

            loadStates.put(name, DependencyNodeState.DISCOVERED); // discovered lol
            // make edges in the graph...
            makeEdges(name, info.dependencies(), DependencyType.DEPEND);
            makeEdges(name, info.loadBefore(), DependencyType.SOFTDEPEND);
        }

        // find undiscovered dependencies.
        boolean hasMissing;
        do {
            var missingDependencies = loadStates.entrySet().stream()
                    .filter(it -> it.getValue() == DependencyNodeState.UNDISCOVERED)
                    .map(Map.Entry::getKey).toList();
            hasMissing = !missingDependencies.isEmpty();
            for (String missingDependency : missingDependencies) {
                loadStates.remove(missingDependency);
                infoMap.remove(missingDependency); // to release their PCL
                var successors = current.successors(missingDependency);
                for (String successor : successors) {
                    var edge = current.edgeConnectingOrNull(missingDependency, successor);
                    if (edge == DependencyType.DEPEND) {
                        log.warn("Cannot load plugin {} because {} is not found or cannot be loaded.", successor, missingDependency);
                        loadStates.put(successor, DependencyNodeState.UNDISCOVERED);
                    } else if (edge == null) {
                        log.error("Impossible null: Cannot find an edge in a closed graph.");
                    }
                }
                current.removeNode(missingDependency);
            }
        }
        while (hasMissing);

        //todo: test if infoMap has some error loading dependencies not released.
        // dependency graph is built! let's load them.
        walk(current, this::instantiate, this::showCommonError);
        walk(current, this::runOnLoad, this::showCommonError);

        // rebuild the graph to run onEnable.
        var nodes = current.nodes();
        // todo:  梳理逻辑，整理和 PluginManager 之间的关系（他们那边也有graph）以及检查 infoMap 是不是都是能用的
        walk(current, this::runOnEnable, this::showCommonError);

    }

    private void runOnEnable(String s) {
        pluginManager.findPlugin(s).orElseThrow().setState(PluginState.ENABLED);
    }

    private void runOnLoad(String s) {
        pluginManager.findPlugin(s).orElseThrow().setState(PluginState.LOADING);
    }

    private void showCommonError(String name, Exception error) {
        if (error == null) {
            log.warn("Cannot load plugin {} due to one of its dependencies doesn't load", name);
        } else {
            log.warn("Cannot load plugin {} : {}", name, error);
        }
        //pluginManager.getPlugins().put(name,PluginState.ERROR_LOADING);
    }

    private void instantiate(String s) throws PluginException {
        var pcl = infoMap.get(s);
        pluginManager.instantiate(pcl);
    }

    private void walk(MutableNetwork<String, DependencyType> graph, OrderedGraphWalker.ExceptionalConsumer<String> consumer, BiConsumer<String, ? super Exception> exceptions) {
        var walker = new OrderedGraphWalker<>(
                graph, s -> {
            if (parent.nodes().contains(s)) return;
            consumer.accept(s);
        }, exceptions, it -> it == DependencyType.DEPEND);
        walker.walk();
        walker.cleanState();
    }

    private void makeEdges(String name, List<String> dependencies, DependencyType depend) {
        for (String dependency : dependencies) {
            loadStates.putIfAbsent(dependency, DependencyNodeState.UNDISCOVERED);
            current.addEdge(dependency, name, depend);
        }
    }

    private void initJarPlugin(Path plugin) {
        try {
            var cl = new PluginClassLoader(plugin, pluginManager.getClass().getClassLoader(), context);
            // check for info
            var info = cl.getPluginInfo();
            if (infoMap.containsKey(info.name())) {
                throw new InvalidPluginInfoException("Plugin with name \"" + info.name() + "\" is already loaded!", plugin);
            }
            if (!PLUGIN_NAME.matcher(info.name()).matches()) {
                throw new InvalidPluginInfoException("Illegal plugin name. Must be " + PLUGIN_NAME.pattern(), plugin);
            }
            var deps = new ArrayList<String>(info.dependencies().size() + info.loadBefore().size());
            deps.addAll(info.dependencies());
            deps.addAll(info.loadBefore());
            for (String dependency : deps) {
                if (!PLUGIN_NAME.matcher(dependency).matches())
                    throw new InvalidPluginInfoException("Illegal dependency name \"" + dependency + "\". Must be " + PLUGIN_NAME.pattern(), plugin);
            }
            infoMap.put(info.name(), cl);
        } catch (InvalidPluginInfoException e) {
            log.warn("Cannot load java plugin {}: {}", plugin.getFileName(), e);
        }
    }
}
