package io.ib67.dash.console.bot;

import io.ib67.dash.AbstractBot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
public class SimpleBotRegistry implements IBotRegistry {
    private final Map<String,AbstractBot> bots = new HashMap<>();
    @Override
    public Collection<? extends AbstractBot> getLoadedBots() {
        return bots.values();
    }

    @Override
    public Optional<? extends AbstractBot> findBot(String bot) {
        return Optional.ofNullable(bots.get(bot));
    }

    @Override
    public void registerBot(AbstractBot bot) {
        if(bots.containsKey(bot)){
            throw new IllegalArgumentException("This bot "+bot+" is already registered.");
        }
        bots.put(bot.getName(),bot);
    }
}
