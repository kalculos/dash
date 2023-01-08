package io.ib67.dash.event.ipc.message;

import io.ib67.dash.message.AbstractMessage;
import io.ib67.dash.message.MessageChain;
import lombok.Getter;

import java.time.Instant;

public class MessageReplyToEvent<M extends AbstractMessage<?>> extends MessageIpcEvent<M> {
    @Getter
    protected final MessageChain response;
    public MessageReplyToEvent(boolean local, Instant time, M message, MessageChain response) {
        super(local, time, message);
        this.response = response;
    }
}
