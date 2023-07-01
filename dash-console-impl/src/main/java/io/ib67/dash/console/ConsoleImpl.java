/*
 * MIT License
 *
 * Copyright (c) 2023 Kalculos and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.ib67.dash.console;

import cloud.commandframework.CommandManager;
import io.ib67.dash.Dash;
import io.ib67.dash.console.command.DashCommandManager;
import io.ib67.dash.console.data.ConsoleInfo;
import io.ib67.dash.console.plugin.IPluginManager;
import io.ib67.dash.console.plugin.SimplePluginManager;
import io.ib67.dash.contact.IContact;
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
    private final CommandManager<IContact> commandManager;
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
