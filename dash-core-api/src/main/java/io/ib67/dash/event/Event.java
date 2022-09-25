package io.ib67.dash.event;

import io.ib67.dash.event.handler.EventHandler;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An abstract presentation of Events.
 */
@ApiStatus.AvailableSince("0.1.0")
public abstract class Event {
    /**
     * {@link EventHandler}s can attach some additional data on the event for communication or sth else.
     */
    @Getter(value = AccessLevel.PUBLIC, lazy = true)
    private final Map<String, Object> context = new ConcurrentHashMap<>();

}
