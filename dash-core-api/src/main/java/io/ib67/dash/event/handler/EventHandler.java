package io.ib67.dash.event.handler;

import io.ib67.dash.event.Event;
import io.ib67.dash.event.IEventChannel;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * An EventHandler receives {@link Event}s
 */
@ApiStatus.AvailableSince("0.1.0")
public interface EventHandler<T extends Event> {

    /**
     * The core part of your handler.
     * <h1> Blocking </h1>
     * In best practices, you're excepted to write non-blocking codes in this method. Otherwise, you stop the world.
     *
     * @return action for eventChannel, cannot be null.
     */
    @NotNull HandleResult handleMessage(@NotNull T event, @NotNull IEventChannel<T> channel);
}
