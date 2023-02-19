package io.ib67.dash.console;

import io.ib67.dash.AbstractBot;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.Optional;

@ApiStatus.AvailableSince("0.1.0")
public interface IBotRegistry {
    Collection<? extends AbstractBot> getLoadedBots();

    Optional<? extends AbstractBot> findBot(String bot);

    void registerBot(AbstractBot bot);
}
