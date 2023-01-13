package io.ib67.dash.event.handler;

import io.ib67.dash.event.AbstractEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A skeleton for listeners who want to listen only.
 * @param <T>
 */
@ApiStatus.AvailableSince("0.1.0")
public abstract class EventHandlerAdapter<T extends AbstractEvent> implements IEventHandler<T>{
    @Override
    public final void handleMessage(@NotNull IEventPipeline<T> pipeline, T event) {
        handleMessage(event);
        pipeline.fireNext();
    }

    public abstract void handleMessage(T event);
}
