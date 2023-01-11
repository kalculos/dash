package io.ib67.dash.event.handler;

import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;

public interface IEventPipeline<E extends AbstractEvent> {
    boolean isCancelled();

    boolean setCancelled(boolean cancel);

    void unsubscribe();

    void fireNext();

    IEventChannel<E> channel();
}
