package io.ib67.dash.event.ipc.message;

import io.ib67.dash.event.ipc.IpcEvent;
import io.ib67.dash.message.AbstractMessage;
import lombok.Getter;

import java.time.Instant;

public abstract class MessageIpcEvent<M extends AbstractMessage<?>> extends IpcEvent {
    @Getter // immutable by default.
    protected M message;
    public MessageIpcEvent(boolean local, Instant time,M message) {
        super(local, time);
        this.message=message;
    }
}
