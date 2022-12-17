package io.ib67.dash.exception;

import io.ib67.dash.event.AbstractEvent;
import lombok.Getter;

@Getter
public class EventException extends RuntimeException {
    private final AbstractEvent involvedEvent;

    public EventException(AbstractEvent involvedEvent) {
        super();
        this.involvedEvent = involvedEvent;
    }

    public EventException(String message, AbstractEvent involvedEvent) {
        super(message);
        this.involvedEvent = involvedEvent;
    }

    public EventException(String message, Throwable cause, AbstractEvent involvedEvent) {
        super(message, cause);
        this.involvedEvent = involvedEvent;
    }

    public EventException(Throwable cause, AbstractEvent involvedEvent) {
        super(cause);
        this.involvedEvent = involvedEvent;
    }

    protected EventException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, AbstractEvent involvedEvent) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.involvedEvent = involvedEvent;
    }
}
