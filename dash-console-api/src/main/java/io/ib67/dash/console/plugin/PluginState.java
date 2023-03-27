package io.ib67.dash.console.plugin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PluginState {
    ERROR_LOADING("ERROR loading", null),
    ERROR_ENABLING("ERROR enabling", null),
    ERROR_DISABLING("ERROR disabling", null),
    LOADING("Loading", PluginState.ERROR_LOADING),
    ENABLED("Enabled", PluginState.ERROR_ENABLING),
    DISABLED("Disabled", PluginState.ERROR_DISABLING);

    private final String localizedName;
    private final PluginState fallbackState;
}
