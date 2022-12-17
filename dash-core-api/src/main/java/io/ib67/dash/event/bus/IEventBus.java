package io.ib67.dash.event.bus;

import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.handler.IEventHandler;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

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
    <E extends AbstractEvent> void register(IEventChannel<E> channel, IEventHandler<E> handler);

    /**
     * Post an event into the bus.
     * Always call this on where the event is created.
     *
     * @param event the event
     */
    <E extends AbstractEvent> void postEvent(E event, Consumer<E> whenDone);
}
