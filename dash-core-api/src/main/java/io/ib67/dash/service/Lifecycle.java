package io.ib67.dash.service;

import org.jetbrains.annotations.ApiStatus;

/**
 * A {@link Lifecycle} is an object that subscribe to server status changes, and react them.
 */
@SuppressWarnings("EmptyMethod")
@ApiStatus.AvailableSince("0.1.0")
public interface Lifecycle {
    /**
     * Where you can initialize some necessary components.
     * Something may not work during this time.
     */
    default void onInitialize() {
    }

    /**
     * Where you should enable your components.
     * Dash is prepared.
     */
    default void onEnable() {
    }

    /**
     * Clean up unclosed resources...
     */
    default void onTerminate() {
    }
}
