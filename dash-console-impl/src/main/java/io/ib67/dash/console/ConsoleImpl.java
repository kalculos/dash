package io.ib67.dash.console;

import cloud.commandframework.CommandManager;
import io.ib67.dash.Dash;
import io.ib67.dash.console.bot.SimpleBotRegistry;
import io.ib67.dash.console.command.DashCommandManager;
import io.ib67.dash.console.data.ConsoleInfo;
import io.ib67.dash.console.plugin.IPluginManager;
import io.ib67.dash.console.plugin.SimplePluginManager;
import io.ib67.dash.contact.Contact;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

public class ConsoleImpl implements IConsole {
    @Getter
    private static final Logger consoleLogger = LoggerFactory.getLogger("CONSOLE");

    @Getter
    private final ConsoleInfo info;
    @Getter
    private final IPluginManager pluginManager;
    @Getter
    private final CommandManager<Contact> commandManager;
    @Getter
    private final IBotRegistry botRegistry;
    @Getter
    private final Dash dash;

    public ConsoleImpl(Dash dash, ConsoleInfo info) {
        requireNonNull(this.info = info);
        requireNonNull(this.dash = dash);
        pluginManager = new SimplePluginManager();
        commandManager = new DashCommandManager(dash.getMainPool(), dash.getPermissionRegistry());
        botRegistry = new SimpleBotRegistry();
    }
}
