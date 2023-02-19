package io.ib67.dash.console.plugin;

import io.ib67.dash.console.plugin.exception.PluginException;
import io.ib67.kiwi.Result;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

public class SimplePluginManager implements IPluginManager{
    @Override
    public Optional<? extends AbstractPlugin> getPluginByName(String name) {
        return Optional.empty();
    }

    @Override
    public Collection<? extends PluginHolder> getPlugins() {
        return null;
    }

    @Override
    public boolean isPluginLoaded(String name) {
        return false;
    }

    @Override
    public Result<AbstractPlugin, ? extends PluginException> loadPlugin(Path pathToPlugin) {
        return null;
    }
}
