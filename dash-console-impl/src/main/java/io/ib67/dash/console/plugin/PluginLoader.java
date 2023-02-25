package io.ib67.dash.console.plugin;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import com.github.zafarkhaja.semver.Version;
import com.google.common.graph.MutableNetwork;
import io.ib67.dash.console.IConsole;
import io.ib67.dash.console.internal.SemVerSerializer;
import io.ib67.dash.console.plugin.exception.PluginException;
import io.ib67.dash.console.plugin.info.PluginInfo;
import io.ib67.kiwi.Kiwi;
import io.ib67.kiwi.tuple.Pair;
import lombok.extern.slf4j.Slf4j;
import org.spongepowered.configurate.ConfigurationNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

@Slf4j
public class PluginLoader {
    private static final TomlMapper TOML = initMapper();
    private static final Pattern PLUGIN_NAME = Pattern.compile("^[a-zA-Z0-9_+]+");

    private static TomlMapper initMapper() {
        var mapper = new TomlMapper();
        var module = new SimpleModule();
        module.addDeserializer(Version.class, new SemVerSerializer());
        mapper.registerModule(module);
        mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        return mapper;
    }

    private final SimplePluginManager pluginManager;
    private final MutableNetwork<String, DependencyType> depNetwork;

    public PluginLoader(SimplePluginManager pluginManager, MutableNetwork<String, DependencyType> depNetwork) {
        this.pluginManager = pluginManager;
        this.depNetwork = depNetwork;
    }

    public void loadPlugins(Path pathPlugins) {
        List<Path> pluginPaths;
        try (var stream = Files.walk(pathPlugins, 1)) {
            pluginPaths = stream.filter(it -> it.getFileName().endsWith(".jar")).toList();
        } catch (IOException e) {
            throw new IllegalStateException("Can't find plugins", e);
        }
        var infoMap = getPluginInfos(pluginPaths);

        // step.1 build dependency graph.
        var stateMap = new HashMap<String, DependencyNodeState>(infoMap.size());
        for (Pair<Path, PluginInfo> pair : infoMap.values()) {
            var info = pair.right;
            var name = info.name().trim();
            if (!PLUGIN_NAME.matcher(name).matches()) {
                log.error("Plugin \"{}\" has a invalid name. It must be {}", name, PLUGIN_NAME.pattern());
            }
            stateMap.put(name, DependencyNodeState.DISCOVERED);
            markDependencies(name, info.dependencies(), DependencyType.DEPEND, stateMap);
            markDependencies(name, info.loadBefore(), DependencyType.SOFTDEPEND, stateMap);
        }
        infoMap = null;

        // step.1.2 find undiscovered dependencies.
        boolean hasMissing;
        do {
            var missingDependencies = stateMap.entrySet().stream()
                    .filter(it -> it.getValue() == DependencyNodeState.UNDISCOVERED)
                    .map(Map.Entry::getKey).toList();
            hasMissing = !missingDependencies.isEmpty();
            for (String missingDependency : missingDependencies) {
                stateMap.remove(missingDependency);
                var successors = depNetwork.successors(missingDependency);
                for (String successor : successors) {
                    var edge = depNetwork.edgeConnectingOrNull(missingDependency, successor);
                    if (edge == DependencyType.DEPEND) {
                        log.warn("Cannot load plugin {} because {} is not found or cannot be loaded.", successor, missingDependency);
                        stateMap.put(successor, DependencyNodeState.UNDISCOVERED);
                    } else if (edge == null) {
                        log.error("Impossible null: Cannot find an edge in a closed graph.");
                    }
                }
                depNetwork.removeNode(missingDependency);
            }
        }
        while (hasMissing);
        stateMap = null; // don't need it anymore

        // step.2 load into dash.
        var loadedPlugin = new HashSet<String>();
        for (String node : depNetwork.nodes()) {
            loadPluginInGraph(node, infoMap, loadedPlugin);
        }
    }

    private boolean loadPluginInGraph(String node, Map<String, Pair<Path, PluginInfo>> infoMap, Set<String> loaded) {
        if (loaded.contains(node)) return true;
        for (String predecessor : depNetwork.predecessors(node)) {
            loadPluginInGraph(predecessor, infoMap, loaded); // tail recursive
        }
        var pair = infoMap.get(node);

        try {
            var plugin = loadPlugin0(pair.left, pair.right);
            var loader = (PluginClassLoader) plugin.getClass().getClassLoader();
            var holder = new JavaPluginHolder(plugin, PluginState.LOADING, loader);
            pluginManager.pluginMap.put(node, holder);
            loaded.add(node);
            return true;
        }catch(Throwable t){
            t.printStackTrace();
            return false;
        }
    }

    private void markDependencies(String name, List<String> dependencies, DependencyType depend, Map<String, DependencyNodeState> stateMap) {
        for (String dependency : dependencies) {
            dependency = dependency.trim();
            if (!PLUGIN_NAME.matcher(dependency).matches()) {
                log.error("Plugin {} has declared a dependency with an invalid name \"{}\".", name, dependency);
            }
            stateMap.putIfAbsent(dependency, DependencyNodeState.UNDISCOVERED);
            depNetwork.addEdge(dependency, name, DependencyType.DEPEND);
        }
    }

    private Map<String, Pair<Path, PluginInfo>> getPluginInfos(List<Path> pluginPaths) {
        var result = new HashMap<String, Pair<Path, PluginInfo>>(pluginPaths.size());
        for (Path pluginPath : pluginPaths) {
            try {
                var info = extractInfo(pluginPath);
                result.put(info.name().trim(), Kiwi.pairOf(pluginPath, info));
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private PluginInfo extractInfo(Path path) {
        try {
            var jarFile = new JarFile(path.toFile());
            var pluginDesc = jarFile.getJarEntry("plugin.toml");
            try (var is = jarFile.getInputStream(pluginDesc)) {
                return TOML.readValue(is, PluginInfo.class);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot find any valid plugin.toml in " + path.getFileName(), e);
        }
    }

    private AbstractPlugin loadPlugin0(Path pluginPath, PluginInfo info) {
        // initialize
        var pcl = new PluginClassLoader(
                pluginPath, info, this.getClass().getClassLoader(), this
        );
        try {
            var clazz = Class.forName(info.main().trim(), true, pcl);
            //PluginInfo info, ConfigurationNode configRoot, Path dataFolder, IConsole console
            var constructor = clazz.getDeclaredConstructor(PluginInfo.class, ConfigurationNode.class,Path.class, IConsole.class);
            // initialize plugin configurations?
        } catch (ClassNotFoundException e) {
            throw new PluginException("Cannot load plugin main " + info.main().trim(), pluginPath);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Cannot find a valid constructor for "+info.main().trim(),e);
        }
    }
}
