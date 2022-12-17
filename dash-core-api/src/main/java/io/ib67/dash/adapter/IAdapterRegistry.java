package io.ib67.dash.adapter;

import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.Optional;

/**
 * Manages {@link PlatformAdapter}
 */
@ApiStatus.AvailableSince("0.1.0")
public interface IAdapterRegistry {
    void registerAdapter(PlatformAdapter adapter);

    Optional<? extends PlatformAdapter> getAdapter(String name);

    Collection<? extends PlatformAdapter> allAdapters();
}
