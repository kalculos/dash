package io.ib67.dash.console.plugin;

import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import io.ib67.dash.console.IConsole;
import io.ib67.dash.console.plugin.exception.PluginException;
import io.ib67.dash.console.plugin.info.PluginInfo;
import io.ib67.kiwi.Kiwi;
import io.ib67.kiwi.future.Result;
import lombok.extern.slf4j.Slf4j;
import org.spongepowered.configurate.ConfigurationNode;

import java.lang.reflect.InvocationTargetException;
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

    @Override
    @SuppressWarnings("unchecked")
    public Result<AbstractPlugin, ? extends PluginException> loadPlugin(Path pathToPlugin) {
        var r1 = Kiwi.fromAny(() -> loader.extractInfo(pathToPlugin));
        if (r1.isFailed()) return (Result<AbstractPlugin, ? extends PluginException>) (Object) r1;
        return loadPlugin0(pathToPlugin, r1.get());
    }

    Result<AbstractPlugin, ? extends PluginException> loadPlugin0(Path pluginPath, PluginInfo info) {
        log.info("Initializing Plugin: {} ({})", info.name(), info.version().toString());
        // initialize
        var pcl = new PluginClassLoader(
                pluginPath, info, this.getClass().getClassLoader()
        );
        try {
            var clazz = Class.forName(info.main().trim(), true, pcl);
            //PluginInfo info, ConfigurationNode configRoot, Path dataFolder, IConsole console
            var constructor = clazz.getDeclaredConstructor(PluginInfo.class, ConfigurationNode.class, Path.class, IConsole.class);
            // initialize plugin configurations?
            var plugin = (AbstractPlugin) constructor.newInstance(); // todo
            pluginMap.put(info.name(), new JavaPluginHolder(plugin, PluginState.LOADING, pcl));
            return Result.ok(plugin);
        } catch (ClassNotFoundException e) {
            return Result.fail(new PluginException("Cannot load plugin main " + info.main().trim(), pluginPath));
        } catch (NoSuchMethodException e) {
            return Result.fail(new PluginException("Cannot find a valid constructor for " + info.main().trim(), e, pluginPath));
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            return Result.fail(new PluginException("Cannot instantiate object", e, pluginPath));
        }
    }

    @Override
    public void loadPlugins(Path pathToPlugins) {
        loader.loadPlugins(pathToPlugins);
    }
}
