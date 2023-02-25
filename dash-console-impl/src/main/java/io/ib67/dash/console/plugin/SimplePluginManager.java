package io.ib67.dash.console.plugin;

import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import io.ib67.dash.console.plugin.exception.PluginException;
import io.ib67.kiwi.Result;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SimplePluginManager implements IPluginManager {

    final Map<String, JavaPluginHolder> pluginMap = new ConcurrentHashMap<>();
    private final MutableNetwork<String, DependencyType> depNetwork = NetworkBuilder.directed().build();
    private final PluginLoader loader = new PluginLoader(this, depNetwork);

    @Override
    public Optional<? extends AbstractPlugin> findPlugin(String name) {
        return Optional.ofNullable(pluginMap.get(name)).map(PluginHolder::getPlugin);
    }

    @Override
    public Collection<JavaPluginHolder> getPlugins() {
        return pluginMap.values();
    }

    @Override
    public boolean isPluginLoaded(String name) {
        return pluginMap.containsKey(name) && pluginMap.get(name).getState() == PluginState.ENABLED;
    }

    JavaPluginHolder getPluginHolder(String name) {
        return pluginMap.get(name);
    }

    @Override
    public Result<AbstractPlugin, ? extends PluginException> loadPlugin(Path pathToPlugin) {
        return null;
    }

    @Override
    public void loadPlugins(Path pathToPlugins) {
        loader.loadPlugins(pathToPlugins);
    }
}
