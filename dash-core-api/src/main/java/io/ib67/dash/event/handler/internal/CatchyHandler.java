package io.ib67.dash.event.handler.internal;

import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.handler.HandleResult;
import io.ib67.dash.event.handler.IEventHandler;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

/**
 * Ugly implementation.
 */
@RequiredArgsConstructor
@ApiStatus.Internal
public final class CatchyHandler<T extends AbstractEvent> implements IEventHandler<T> {
    private final BiConsumer<AbstractEvent, IEventChannel<T>> handler;

    private boolean handled;

    @Override
    public @NotNull HandleResult handleMessage(@NotNull T event, @NotNull IEventChannel<T> channel) {
        if (handled) return HandleResult.UNSUBSCRIBE;
        handler.accept(event, channel);
        handled = true;
        return HandleResult.CANCELLED;
    }
}
