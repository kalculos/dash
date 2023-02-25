package io.ib67.dash.console.plugin;

import io.ib67.dash.console.plugin.exception.PluginException;
import io.ib67.kiwi.future.Result;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

public interface IPluginManager {
    Optional<? extends AbstractPlugin> findPlugin(String name);

    Collection<? extends PluginHolder> getPlugins();

    boolean isPluginLoaded(String name);

    Result<AbstractPlugin, ? extends PluginException> loadPlugin(Path pathToPlugin);

    void loadPlugins(Path pathToPlugins);
}
