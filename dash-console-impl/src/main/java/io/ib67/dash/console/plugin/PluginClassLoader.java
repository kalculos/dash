package io.ib67.dash.console.plugin;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import com.github.zafarkhaja.semver.Version;
import io.ib67.dash.console.internal.SemVerSerializer;
import io.ib67.dash.console.plugin.exception.InvalidPluginInfoException;
import io.ib67.dash.console.plugin.info.PluginInfo;
import io.ib67.dash.console.plugin.java.SharedClassContext;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

import static java.util.Objects.requireNonNull;

public final class PluginClassLoader extends URLClassLoader {
    private static final TomlMapper INFO_MAPPER = initMapper();
    @Getter
    private final Path pluginFile;
    @Getter
    private final SharedClassContext sharedContext;
    @Getter
    private final PluginInfo pluginInfo;

    @SneakyThrows
    public PluginClassLoader(Path url, ClassLoader parent, SharedClassContext sharedClassContext) throws InvalidPluginInfoException{
        super(new URL[]{url.toUri().toURL()}, parent);
        requireNonNull(pluginFile = url);
        this.sharedContext = sharedClassContext;
        pluginInfo = loadPluginInfo();
    }

    private PluginInfo loadPluginInfo() throws InvalidPluginInfoException {
        try (var is = getResourceAsStream("plugin.toml")) {
            return INFO_MAPPER.readValue(is, PluginInfo.class);
        } catch (IOException e) {
            throw new InvalidPluginInfoException("Invalid plugin.toml", e, pluginFile);
        }
    }

    private static TomlMapper initMapper() {
        var mapper = new TomlMapper();
        var module = new SimpleModule();
        module.addDeserializer(Version.class, new SemVerSerializer());
        mapper.registerModule(module);
        mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        return mapper;
    }
}
