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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Getter
@AllArgsConstructor
@Slf4j(topic = "Plugin Loader")
public class PluginHolder {
    protected AbstractPlugin plugin;
    protected PluginState state;

    public boolean setState(PluginState newState) {
        if (newState == state) return true;
        try {
            switch (newState) {
                case LOADING -> {
                    plugin.onInitialize();
                }
                case ENABLED -> {
                    plugin.onEnable();
                }
                case DISABLED -> {
                    plugin.onTerminate();
                }
            }
            state = newState;
            return true;
        } catch (Exception e) {
            log.warn("Can't load plugin \"{}\" during {} state", plugin.getInfo().name(), newState, e);
            Objects.requireNonNull(newState.getFallbackState(), "Impossible null");
            state = newState.getFallbackState();
            return false;
        }
    }
}
