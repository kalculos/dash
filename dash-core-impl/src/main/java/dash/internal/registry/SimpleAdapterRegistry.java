package dash.internal.registry;

import io.ib67.dash.adapter.IAdapterRegistry;
import io.ib67.dash.adapter.PlatformAdapter;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleAdapterRegistry implements IAdapterRegistry {
    private final Map<String, PlatformAdapter> adapters = new ConcurrentHashMap<>();

    @Override
    public void registerAdapter(PlatformAdapter adapter) {
        adapters.put(adapter.getName(), adapter);
    }

    @Override
    public Optional<? extends PlatformAdapter> getAdapter(String name) {
        return Optional.ofNullable(adapters.get(name));
    }

    @Override
    public Collection<? extends PlatformAdapter> allAdapters() {
        return adapters.values();
    }
}
