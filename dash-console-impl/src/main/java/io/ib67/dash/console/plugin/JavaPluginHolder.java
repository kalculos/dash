package io.ib67.dash.console.plugin;

import lombok.Getter;

import java.util.Objects;

@Getter
public class JavaPluginHolder extends PluginHolder{
    protected final PluginClassLoader classLoader;

    public JavaPluginHolder(AbstractPlugin plugin, PluginState state, PluginClassLoader classLoader) {
        super(plugin, state);
        Objects.requireNonNull(this.classLoader = classLoader);
    }
}
