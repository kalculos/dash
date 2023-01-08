package io.ib67.dash.event.ipc.message;

import io.ib67.dash.message.AbstractMessage;
import org.jetbrains.annotations.ApiStatus;

import java.time.Instant;

@ApiStatus.AvailableSince("0.1.0") //fixme: maybe useless.
public class MessagePostSendEvent<M extends AbstractMessage<?>> extends MessageIpcEvent<M> {

    public MessagePostSendEvent(boolean local, Instant time, M message) {
        super(local, time, message);
    }
}
