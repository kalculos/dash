package io.ib67.dash.console.plugin;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PluginHolder {
    protected AbstractPlugin plugin;
    protected PluginState state;

    public void setState(PluginState newState){
        switch (newState){
            case LOADING -> {

            }
        }
    }
}
