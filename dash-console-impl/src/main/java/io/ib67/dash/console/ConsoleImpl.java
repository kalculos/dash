package io.ib67.dash.console;

import cloud.commandframework.CommandManager;
import io.ib67.dash.Dash;
import io.ib67.dash.console.command.DashCommandManager;
import io.ib67.dash.console.data.ConsoleInfo;
import io.ib67.dash.console.plugin.IPluginManager;
import io.ib67.dash.console.plugin.SimplePluginManager;
import io.ib67.dash.contact.Contact;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;

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
    private final Dash dash;

    @SneakyThrows
    public ConsoleImpl(Dash dash, ConsoleInfo info) {
        requireNonNull(this.info = info);
        requireNonNull(this.dash = dash);
        Path plugins = Path.of("plugins");
        if(Files.notExists(plugins)){
            Files.createDirectory(plugins);
        }
        pluginManager = new SimplePluginManager(plugins,this);
        commandManager = new DashCommandManager(dash.getScheduler(), dash.getPermissionRegistry());

    }
}
