package io.ib67.dash.event.bus;

import io.ib67.dash.event.Event;
import io.ib67.dash.event.handler.EventHandler;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;

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
     * @param <N>     converted message or null. handler won't be called when null is present.
     */
    @ApiStatus.Internal
    <E extends Event, N extends Event> void register(Function<E, N> channel, EventHandler<N> handler);

    /**
     * Post an event into the bus.
     * Always call this on where the event is created.
     *
     * @param event the event
     */
    void postEvent(Event event);
}
