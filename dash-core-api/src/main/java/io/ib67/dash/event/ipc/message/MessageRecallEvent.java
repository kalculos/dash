package io.ib67.dash.event.ipc.message;

import io.ib67.dash.message.AbstractMessage;

import java.time.Instant;

public class MessageRecallEvent extends MessageIpcEvent{
    public MessageRecallEvent(boolean local, Instant time, AbstractMessage message) {
        super(local, time, message);
    }
}
