package io.ib67.dash.console.plugin.exception;

import lombok.Getter;

import java.nio.file.Path;

@Getter
public class PluginException extends Exception{
    protected final Path pathToPlugin;
    public PluginException(Path pathToPlugin) {
        super();
        this.pathToPlugin = pathToPlugin;
    }

    public PluginException(String message, Path pathToPlugin) {
        super(message);
        this.pathToPlugin = pathToPlugin;
    }

    public PluginException(String message, Throwable cause, Path pathToPlugin) {
        super(message, cause);
        this.pathToPlugin = pathToPlugin;
    }

    public PluginException(Throwable cause, Path pathToPlugin) {
        super(cause);
        this.pathToPlugin = pathToPlugin;
    }

    protected PluginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Path pathToPlugin) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.pathToPlugin = pathToPlugin;
    }
}
