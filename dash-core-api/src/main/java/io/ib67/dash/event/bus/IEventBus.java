package io.ib67.dash.event.bus;

import io.ib67.dash.event.Event;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.handler.EventHandler;
import org.jetbrains.annotations.ApiStatus;

/**
 * An IEventBus delivers event to its registered handlers.
 */
@ApiStatus.AvailableSince("0.1.0")
public interface IEventBus {
    /**
     * Registers an listener
     *
     * @param channel the real channel
     * @param handler message handler
     * @param <E>     original message
     */
    @ApiStatus.Internal
    <E extends Event> void register(IEventChannel<E> channel, EventHandler<E> handler);

    /**
     * Post an event into the bus.
     * Always call this on where the event is created.
     *
     * @param event the event
     */
    void postEvent(Event event);
}
