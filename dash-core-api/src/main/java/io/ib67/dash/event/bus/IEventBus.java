package io.ib67.dash.event.bus;

import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannelFactory;
import io.ib67.dash.event.IEventRegistry;
import io.ib67.kiwi.Result;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

/**
 * An IEventBus delivers event to its registered handlers.
 */
@ApiStatus.AvailableSince("0.1.0")
public interface IEventBus extends IEventRegistry {
    /**
     * Post an event into the bus.
     * Always call this on where the event is created.
     *
     * @param event the event
     */
    <E extends AbstractEvent> void postEvent(E event, Consumer<Result<E,?>> whenDone);

    /**
     * Gets the channel factory belonging to this bus
     * @return channel factory
     */
    IEventChannelFactory getChannelFactory();
}
