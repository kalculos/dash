package io.ib67.dash.event;

import io.ib67.dash.AbstractBot;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.AvailableSince("0.1.0")
public interface IEventRegistry {
    void registerListeners(AbstractBot bot, EventListener listener);
}
