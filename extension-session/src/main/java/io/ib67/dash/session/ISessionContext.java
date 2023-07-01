package io.ib67.dash.session;

import io.ib67.dash.message.IMessageSource;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

import java.util.Collection;
import java.util.Queue;

@ApiStatus.AvailableSince("0.1.0")
public interface ISessionContext {
    ISessionState getState();

    Collection<? extends IMessageSource> getParticipants();

    IMessageSource getMessageSource();

    @Contract("-> new")
    Queue<? extends ISessionState> getStateChain();

    void switchState(InitReason reason, ISessionState newState);

    void stepBack();

    String getName();

}
