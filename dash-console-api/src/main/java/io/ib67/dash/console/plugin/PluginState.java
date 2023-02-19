package io.ib67.dash.console.plugin;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PluginState {
    LOADING("Loading"),
    ENABLED("Enabled"),
    DISABLED("Disabled"),
    ERROR_LOADING("ERROR loading"),
    ERROR_ENABLING("ERROR enabling"),
    ERROR_DISABLING("ERROR disabling");

    private final String localizedName;
}
