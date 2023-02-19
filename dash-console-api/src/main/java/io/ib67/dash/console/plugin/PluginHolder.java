package io.ib67.dash.console.plugin;

import lombok.Getter;
import lombok.Setter;

import static java.util.Objects.requireNonNull;

@Getter
public class PluginHolder {
    private final AbstractPlugin plugin;
    @Setter
    private PluginState state;

    public PluginHolder(AbstractPlugin plugin, PluginState initialState) {
        requireNonNull(this.plugin = plugin);
        requireNonNull(this.state = initialState);
    }
}
