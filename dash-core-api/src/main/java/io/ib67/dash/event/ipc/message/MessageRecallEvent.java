package io.ib67.dash.event.ipc.message;

import io.ib67.dash.message.AbstractMessage;

import java.time.Instant;

public class MessageRecallEvent<M extends AbstractMessage<?>> extends MessageIpcEvent<M>{
    public MessageRecallEvent(boolean local, Instant time, M message) {
        super(local, time, message);
    }
}
