package io.ib67.dash.console.plugin;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PluginHolder {
    protected AbstractPlugin plugin;
    protected PluginState state;
}
