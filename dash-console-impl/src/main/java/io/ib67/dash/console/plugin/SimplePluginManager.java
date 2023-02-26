package io.ib67.dash.console.plugin;

import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import io.ib67.dash.console.IConsole;
import io.ib67.dash.console.plugin.exception.IllegalPluginException;
import io.ib67.dash.console.plugin.exception.InvalidPluginInfoException;
import io.ib67.dash.console.plugin.exception.PluginException;
import io.ib67.dash.console.plugin.info.PluginInfo;
import io.ib67.dash.console.plugin.loader.DependencyType;
import io.ib67.kiwi.Kiwi;
import io.ib67.kiwi.future.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
public class SimplePluginManager implements IPluginManager {
    private final Map<String, PluginHolder> pluginMap = new ConcurrentHashMap<>();
    @SuppressWarnings("all")
    private final MutableNetwork<String, DependencyType> depNetwork = NetworkBuilder.directed().build();
    private final IConsole console;
    private final Path pluginDataRoot;


    @Override
    public Optional<? extends PluginHolder> findPlugin(String name) {
        return Optional.ofNullable(pluginMap.get(name));
    }

    @Override
    public Collection<PluginHolder> getPlugins() {
        return pluginMap.values();
    }

    @Override
    public boolean isPluginLoaded(String name) {
        return pluginMap.containsKey(name) && pluginMap.get(name).getState() == PluginState.ENABLED;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Result<AbstractPlugin, ? extends PluginException> loadPlugin(Path pathToPlugin) {
        return Kiwi.todo();
    }

    @Override
    public void loadPlugins(List<Path> pathToPlugins) {
        new BatchPluginLoader(depNetwork, this, null).load(pathToPlugins);
    }

    PluginHolder instantiate(PluginClassLoader pcl) throws PluginException {
        var main = pcl.getPluginInfo().main();
        var name = pcl.getPluginInfo().name();
        try {
            var clazz = Class.forName(main, true, pcl);
            if (!AbstractPlugin.class.isAssignableFrom(clazz)) {
                throw new IllegalPluginException("Main class \"" + clazz + "\" from plugin " + name + " is not any subtype of AbstractPlugin.", pcl.getPluginFile());
            }
            var defaultConstructor = clazz.getDeclaredConstructor(PluginInfo.class, Path.class, IConsole.class);
            var dataFolder = pluginDataRoot.resolve(name);
            if (!Files.isDirectory(dataFolder)) {
                Files.createDirectory(dataFolder);
            }
            var pluginObj = (AbstractPlugin) defaultConstructor.newInstance(pcl.getPluginInfo(), dataFolder, console);
            pluginMap.put(name, new PluginHolder(pluginObj, null));
            return pluginMap.get(name);
        } catch (ClassNotFoundException e) {
            throw new InvalidPluginInfoException("Main class \"" + main + "\" isn't found for plugin " + name, e, pcl.getPluginFile());
        } catch (NoSuchMethodException e) {
            throw new IllegalPluginException("Can't find the default constructor for the class.", e, pcl.getPluginFile());
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalPluginException("Can't instantitate plugin object for " + name, e, pcl.getPluginFile());
        } catch (IOException e) {
            throw new IllegalStateException("Cannot create datafolder for " + name);
        }
    }
}
