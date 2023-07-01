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

package io.ib67.dash.console.plugin;

import io.ib67.dash.console.IConsole;
import io.ib67.dash.console.plugin.info.PluginInfo;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

import static java.util.Objects.requireNonNull;

public abstract class AbstractPlugin{
    @Getter
    private final PluginInfo info;
    @Getter
    private final Logger logger;
    @Getter
    private final Path dataFolder;
    @Getter
    private final IConsole console;

    /**
     * You'll have to modify codes in BatchPluginLoader as well.
     */
    public AbstractPlugin(PluginInfo info, Path dataFolder, IConsole console) {
        this.info = info;
        requireNonNull(this.dataFolder = dataFolder);
        requireNonNull(this.console = console);
        logger = LoggerFactory.getLogger(info.name());
    }

    /**
     * Only for a cleaner overload
     */
    public AbstractPlugin() {
        throw new IllegalStateException("This constructor should never be called.");
    }

    public void onInitialize(){}

    public void onEnable(){}

    public void onTerminate(){}
}
