package io.ib67.dash.event;

import io.ib67.dash.message.ContextKey;
import io.ib67.dash.message.IMessageContext;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.AvailableSince("0.1.0")
public interface ContextualEvent {
    IMessageContext getContext();
    boolean hasContext(ContextKey<?> key);
}
