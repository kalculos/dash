package io.ib67.dash.console.plugin;

import io.ib67.dash.console.IConsole;
import io.ib67.dash.console.plugin.loader.PluginInitializer;
import io.ib67.kiwi.Kiwi;
import io.ib67.kiwi.tuple.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static io.ib67.kiwi.Kiwi.pairOf;

@Slf4j
@RequiredArgsConstructor
public class SimplePluginManager implements IPluginManager {
    private final Map<String, PluginHolder> pluginMap = new ConcurrentHashMap<>();

    private final Path pluginDataRoot;
    private final IConsole console;

    @Override
    public Collection<Pair<PluginState, AbstractPlugin>> getPlugins() {
        return pluginMap.values().stream().map(entr -> pairOf(entr.getState(), entr.getPlugin())).toList();
    }

    @Override
    @SuppressWarnings("unchecked") // user guarantees this type-safety.
    public <P extends AbstractPlugin> Optional<P> getPluginById(String id) {
        return Optional.ofNullable((P) pluginMap.get(id).getPlugin());
    }

    @Override
    public PluginHolder registerPlugin(AbstractPlugin plugin) {
        if (pluginMap.containsKey(plugin.getInfo().name())) {
            throw new IllegalArgumentException(plugin.getInfo().name() + " is already registered");
        }
        var newHolder = new PluginHolder(plugin, null);
        pluginMap.put(plugin.getInfo().name(), newHolder);
        return newHolder;
    }

    @Override
    public void loadPlugins(List<Path> pluginsList) {
        new PluginInitializer(this, console, Kiwi.todo("SharedClassContext"), pluginDataRoot).load(pluginsList);
    }
}
