package io.ib67.dash.console.plugin;

import com.github.zafarkhaja.semver.Version;
import io.ib67.dash.console.internal.SemVerSerializer;
import io.ib67.dash.console.plugin.exception.InvalidPluginInfoException;
import io.ib67.dash.console.plugin.info.PluginInfo;
import io.ib67.dash.console.plugin.java.SharedClassContext;
import lombok.Getter;
import lombok.SneakyThrows;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

import static java.util.Objects.requireNonNull;

public final class PluginClassLoader extends URLClassLoader {
    @Getter
    private final Path pluginFile;
    @Getter
    private final SharedClassContext sharedContext;
    @Getter
    private final PluginInfo pluginInfo;

    @SneakyThrows
    public PluginClassLoader(Path url, ClassLoader parent, SharedClassContext sharedClassContext) throws InvalidPluginInfoException {
        super(new URL[]{url.toUri().toURL()}, parent);
        requireNonNull(pluginFile = url);
        this.sharedContext = sharedClassContext;
        pluginInfo = loadPluginInfo();
    }

    private PluginInfo loadPluginInfo() throws InvalidPluginInfoException {
        try (var is = getResourceAsStream("plugin.yml")) {
            if(is == null) throw new IOException("Cannot found plugin.yml");
            return YamlConfigurationLoader.builder()
                    .defaultOptions(op -> op.serializers(it -> it.register(Version.class, new SemVerSerializer())))
                    .buildAndLoadString(new String(is.readAllBytes())).get(PluginInfo.class);
        } catch (IOException e) {
            throw new InvalidPluginInfoException("Invalid plugin.yml", e, pluginFile);
        }
    }
}
