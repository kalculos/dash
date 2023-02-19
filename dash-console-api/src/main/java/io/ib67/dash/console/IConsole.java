package io.ib67.dash.console;

import cloud.commandframework.CommandManager;
import io.ib67.dash.Dash;
import io.ib67.dash.console.data.ConsoleInfo;
import io.ib67.dash.console.plugin.IPluginManager;
import io.ib67.dash.contact.Contact;
import org.slf4j.Logger;

public interface IConsole {
    ConsoleInfo getInfo();

    Logger getConsoleLogger();

    IPluginManager getPluginManager();

    CommandManager<Contact> getCommandManager();

    IBotRegistry getBotRegistry();

    Dash getDash();
}
