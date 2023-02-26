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

package io.ib67.dash.console.plugin.exception;

import org.jetbrains.annotations.ApiStatus;

import java.nio.file.Path;

/**
 * Will be thrown when a plugin is not implemented correctly, such as private constructors, incorrect types.
 */
@ApiStatus.AvailableSince("0.1.0")
public class IllegalPluginException extends PluginException{
    public IllegalPluginException(Path pathToPlugin) {
        super(pathToPlugin);
    }

    public IllegalPluginException(String message, Path pathToPlugin) {
        super(message, pathToPlugin);
    }

    public IllegalPluginException(String message, Throwable cause, Path pathToPlugin) {
        super(message, cause, pathToPlugin);
    }

    public IllegalPluginException(Throwable cause, Path pathToPlugin) {
        super(cause, pathToPlugin);
    }

    protected IllegalPluginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Path pathToPlugin) {
        super(message, cause, enableSuppression, writableStackTrace, pathToPlugin);
    }
}
