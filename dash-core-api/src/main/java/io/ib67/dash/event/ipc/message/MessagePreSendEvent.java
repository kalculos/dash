package io.ib67.dash.event.ipc.message;

import io.ib67.dash.message.AbstractMessage;
import org.jetbrains.annotations.ApiStatus;

import java.time.Instant;

/**
 * A MessagePreSendEvent is sent before
 */
@ApiStatus.AvailableSince("0.1.0")
public class MessagePreSendEvent<M extends AbstractMessage<?>> extends MessageIpcEvent<M> {

    public MessagePreSendEvent(boolean local, Instant time, M message) {
        super(local, time, message);
    }

}
