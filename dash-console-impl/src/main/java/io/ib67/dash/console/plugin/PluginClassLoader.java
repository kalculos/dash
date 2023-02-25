package io.ib67.dash.console.plugin;

import io.ib67.dash.console.plugin.info.PluginInfo;
import io.ib67.kiwi.Kiwi;
import lombok.SneakyThrows;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

public class PluginClassLoader extends URLClassLoader {

    private final PluginInfo pluginInfo;
    private final Path pluginFile;
    private final PluginLoader loader;
    private final Map<String,Class<?>> classes = new ConcurrentHashMap<>();
    @SneakyThrows
    public PluginClassLoader(Path url, PluginInfo pluginInfo, ClassLoader parent, PluginLoader loader) {
        super(new URL[]{url.toUri().toURL()}, parent);
        requireNonNull(this.loader = loader);
        requireNonNull(pluginFile = url);
        this.pluginInfo = pluginInfo;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return classes.computeIfAbsent(name,it->findClass0(it,true));
    }

    private Class<?> findClass0(String name, boolean global) {
        try {
            return super.findClass(name); // try to find class in current ucp
        } catch (ClassNotFoundException ignored) {

        }
        // find in global scope;
        return Kiwi.todo();
    }
}
