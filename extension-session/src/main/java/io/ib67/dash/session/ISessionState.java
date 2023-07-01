package io.ib67.dash.session;

import io.ib67.dash.message.AbstractMessage;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.AvailableSince("0.1.0")
public interface ISessionState {
    void init(InitReason reason, ISessionState oldState);

    void onMessage(AbstractMessage<?> message);

    void terminate(ISessionState newState);
}
