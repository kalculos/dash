package io.ib67.dash.exception;

import io.ib67.dash.event.Event;
import lombok.Getter;

@Getter
public class EventException extends RuntimeException {
    private final Event involvedEvent;

    public EventException(Event involvedEvent) {
        super();
        this.involvedEvent = involvedEvent;
    }

    public EventException(String message, Event involvedEvent) {
        super(message);
        this.involvedEvent = involvedEvent;
    }

    public EventException(String message, Throwable cause, Event involvedEvent) {
        super(message, cause);
        this.involvedEvent = involvedEvent;
    }

    public EventException(Throwable cause, Event involvedEvent) {
        super(cause);
        this.involvedEvent = involvedEvent;
    }

    protected EventException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Event involvedEvent) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.involvedEvent = involvedEvent;
    }
}
