package io.ib67.dash.console;

import cloud.commandframework.CommandManager;
import io.ib67.dash.console.data.ConsoleInfo;
import io.ib67.dash.console.plugin.IPluginManager;
import io.ib67.dash.contact.IContact;

public interface IConsole {
    ConsoleInfo getInfo();

    IPluginManager getPluginManager();

    CommandManager<? extends IContact> getCommandManager();
}
