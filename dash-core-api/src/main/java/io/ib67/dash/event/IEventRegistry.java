package io.ib67.dash.event;

import io.ib67.dash.AbstractBot;
import io.ib67.dash.event.handler.IEventHandler;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.AvailableSince("0.1.0")
public interface IEventRegistry {
    /**
     * Register methods annotated with {@link EventHandler} in the listener object.
     * @param bot user
     * @param listener listener
     */
    void registerListeners(AbstractBot bot, EventListener listener);
    /**
     * Registers an listener
     *
     * @param channel the real channel
     * @param handler message handler
     * @param <E>     original message
     */
    @ApiStatus.Internal
    <E extends AbstractEvent> void register(IEventChannel<E> channel, IEventHandler<E> handler);

}
