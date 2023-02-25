package io.ib67.dash.console.plugin;

import io.ib67.dash.AbstractBot;
import io.ib67.dash.BotContext;
import io.ib67.dash.console.IConsole;
import io.ib67.dash.console.plugin.info.PluginInfo;
import io.ib67.dash.service.Lifecycle;
import io.ib67.kiwi.Kiwi;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.configurate.ConfigurationNode;

import java.nio.file.Path;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public abstract class AbstractPlugin implements Lifecycle {
    @Getter
    private final PluginInfo info;
    @Getter
    private ConfigurationNode configRoot;
    @Getter
    private final Logger logger;
    @Getter
    private final Path dataFolder;
    @Getter
    private final IConsole console;

    /**
     * You'll have to modify codes in PluginLoader as well.
     */
    public AbstractPlugin(PluginInfo info, ConfigurationNode configRoot, Path dataFolder, IConsole console) {
        this.info = info;
        requireNonNull(this.dataFolder = dataFolder);
        requireNonNull(this.configRoot = configRoot);
        requireNonNull(this.console = console);
        logger = LoggerFactory.getLogger(info.name());
    }

    public AbstractPlugin() {
        throw new IllegalStateException("This constructor should never be called.");
    }

    protected void installBot(Function<BotContext, AbstractBot> botFactory) {
        Kiwi.todo();
    }
}
