package io.ib67.dash.console.plugin;

import io.ib67.dash.console.plugin.info.PluginInfo;
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
    private final Map<String,Class<?>> classes = new ConcurrentHashMap<>();
    @SneakyThrows
    public PluginClassLoader(Path url, PluginInfo pluginInfo, ClassLoader parent) {
        super(new URL[]{url.toUri().toURL()}, parent);
        requireNonNull(pluginFile = url);
        this.pluginInfo = pluginInfo;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return classes.computeIfAbsent(name,it->findClass0(it,true));
    }
}
