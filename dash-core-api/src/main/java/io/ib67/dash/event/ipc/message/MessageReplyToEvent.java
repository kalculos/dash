package io.ib67.dash.event.ipc.message;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.ib67.dash.message.AbstractMessage;
import io.ib67.dash.message.MessageChain;
import lombok.Getter;

import java.time.Instant;

public class MessageReplyToEvent<M extends AbstractMessage<?>> extends MessageIpcEvent<M> {
    @Getter
    protected final MessageChain response;
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public MessageReplyToEvent(boolean local, Instant time, M message, MessageChain response) {
        super(local, time, message);
        this.response = response;
    }
}
