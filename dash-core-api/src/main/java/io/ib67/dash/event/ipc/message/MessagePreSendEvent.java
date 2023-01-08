package io.ib67.dash.event.ipc.message;

import io.ib67.dash.event.ipc.IpcEvent;
import io.ib67.dash.message.AbstractMessage;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.ApiStatus;

import java.time.Instant;

/**
 * A MessagePreSendEvent is sent before
 */
@ApiStatus.AvailableSince("0.1.0")
public class MessagePreSendEvent<M extends AbstractMessage<?>> extends IpcEvent {
    @Getter
    @Setter
    protected M message;

    public MessagePreSendEvent(boolean local, Instant time, M message) {
        super(local, time);
        this.message = message;
    }

    public MessagePostSendEvent<M> toPostSend() {
        return new MessagePostSendEvent<>(local, time, message);
    }
}
