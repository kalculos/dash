package io.ib67.dash.console.plugin;

import io.ib67.dash.console.plugin.exception.PluginException;
import io.ib67.kiwi.future.Future;
import io.ib67.kiwi.future.Result;
import io.ib67.kiwi.tuple.Pair;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.ApiStatus.AvailableSince;

@AvailableSince("0.1.0")
public interface IPluginManager {
    Collection<Pair<PluginState, AbstractPlugin>> getPlugins();

    <P extends AbstractPlugin> Optional<P> getPluginById(String id);

    PluginHolder registerPlugin(AbstractPlugin plugin);

    PluginState getGlobalState();

    void loadPlugins(List<Path> pluginsList);
}
