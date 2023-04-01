package io.ib67.dash.console.plugin;

import io.ib67.kiwi.tuple.Pair;
import org.jetbrains.annotations.ApiStatus.AvailableSince;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@AvailableSince("0.1.0")
public interface IPluginManager {
    Collection<Pair<PluginState, AbstractPlugin>> getPlugins();

    <P extends AbstractPlugin> Optional<P> getPluginById(String id);

    PluginHolder registerPlugin(AbstractPlugin plugin);

    void loadPlugins(List<Path> pluginsList);
}
