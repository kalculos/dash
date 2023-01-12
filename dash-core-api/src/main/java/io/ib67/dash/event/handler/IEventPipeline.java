package io.ib67.dash.event.handler;

import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;
import org.jetbrains.annotations.ApiStatus;

/**
 * An {@link IEventPipeline} represents handlers for an event. {@link IEventHandler}s uses this to decide event delivery.
 * @param <E>
 */
@ApiStatus.AvailableSince("0.1.0")
public interface IEventPipeline<E extends AbstractEvent> {

    /**
     * Don't call me anymore.
     */
    void unsubscribe();

    /**
     * Pass this to next handler.
     */
    void fireNext();

    IEventChannel<E> channel();
}
