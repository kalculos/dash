package io.ib67.dash.event.handler;

import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.ScheduleType;
import org.jetbrains.annotations.ApiStatus;

/**
 * There results determines whether the event will be processed.
 *
 * <br>All of these will not work when you're using {@link ScheduleType#ASYNC}.</br>
 */
@ApiStatus.AvailableSince("0.1.0")
public enum HandleResult {
    /**
     * The {@link IEventChannel} won't call more handlers.
     * <p>
     */
    CANCELLED,
    /**
     * The {@link IEventChannel} will call next handler.
     */
    CONTINUE,
    /**
     * When {@link #UNSUBSCRIBE} is given, your handler cannot receive events anymore.
     */
    UNSUBSCRIBE
}
