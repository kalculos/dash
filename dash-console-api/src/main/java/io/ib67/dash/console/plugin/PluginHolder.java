package io.ib67.dash.console.plugin;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@Slf4j(topic = "Plugin Loader")
public class PluginHolder {
    protected AbstractPlugin plugin;
    protected PluginState state;

    public void setState(PluginState newState) {
        try {
            switch (newState) {
                case LOADING -> {
                    plugin.onInitialize();
                }
                case ENABLED -> {
                    plugin.onEnable();
                }
                case DISABLED -> {
                    plugin.onTerminate();
                }
            }
        } catch (Exception e) {
            log.warn("Can't load plugin \"{}\" during {} state: {}", plugin.getInfo().name(), newState, e);
            Objects.requireNonNull(newState.getFallbackState(), "Impossible null");
            state = newState.getFallbackState();
        }
    }
}
