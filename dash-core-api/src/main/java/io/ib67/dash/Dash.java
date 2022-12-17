package io.ib67.dash;

import io.ib67.dash.adapter.IAdapterRegistry;
import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.internal.DashInstFiner;
import org.jetbrains.annotations.ApiStatus;

/**
 * The core part of dash framework.<br>
 * You can find everything you need in this class. They are shared among multiple {@link AbstractBot}s.
 */
@ApiStatus.AvailableSince("0.1.0")
public interface Dash {
    static Dash getInstance() {
        return DashInstFiner.FINDER.get();
    }

    IAdapterRegistry getAdapterRegistry();

    /**
     * Where you can receive all events.
     *
     * @return global event channel.
     */
    IEventChannel<? extends AbstractEvent> getGlobalChannel();
}
