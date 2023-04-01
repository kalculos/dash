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

package io.ib67.dash.console.plugin.loader;

import io.ib67.dash.console.IConsole;
import io.ib67.dash.console.plugin.AbstractPlugin;
import io.ib67.dash.console.plugin.IPluginManager;
import io.ib67.dash.console.plugin.PluginHolder;
import io.ib67.dash.console.plugin.PluginState;
import io.ib67.dash.console.plugin.exception.IllegalPluginException;
import io.ib67.dash.console.plugin.exception.InvalidPluginInfoException;
import io.ib67.dash.console.plugin.exception.PluginException;
import io.ib67.dash.console.plugin.info.PluginInfo;
import io.ib67.dash.console.plugin.java.SharedClassContext;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static java.util.Objects.requireNonNull;

@Slf4j
public class PluginInitializer extends PluginLoader {
    private final Path pluginDataRoot;
    private final Map<String, PluginHolder> pluginHolders;

    public PluginInitializer(IPluginManager pluginManager, IConsole console, SharedClassContext sharedClassContext, Path pluginDataRoot) {
        super(pluginManager, console, sharedClassContext);
        requireNonNull(this.pluginDataRoot = pluginDataRoot);
        pluginHolders = new HashMap<>(dependencyNodes.size());
    }

    @Override
    protected void initializePlugins() {
        // initialize them all
        walk(this::initialize, this::showCommonError);
        walk(s -> updateState(s, PluginState.LOADING), this::showCommonError);
        walk(s -> updateState(s, PluginState.ENABLED), this::showCommonError);
    }

    private void updateState(String s, PluginState state) {
        var holder = pluginHolders.get(s);
        assert holder.getState().getFallbackState() != null; // to ensure the given plugin is usable.
        if (!pluginHolders.get(s).setState(state)) {
            throw new IllegalStateException("Can't set plugin \"" + s + "\" to " + state);
        }
    }

    private void initialize(String s) throws PluginException {
        var pcl = dependencyNodes.get(s).classLoader;
        var main = pcl.getPluginInfo().main();
        var name = pcl.getPluginInfo().name();
        try {
            var clazz = Class.forName(main, true, pcl);
            if (!AbstractPlugin.class.isAssignableFrom(clazz)) {
                throw new IllegalPluginException("Main class \"" + clazz + "\" from plugin " + name + " is not any subtype of AbstractPlugin.", pcl.getPluginFile());
            }
            var defaultConstructor = clazz.getDeclaredConstructor(PluginInfo.class, Path.class, IConsole.class);
            var dataFolder = pluginDataRoot.resolve(name);
            if (!Files.isDirectory(dataFolder)) {
                Files.createDirectory(dataFolder);
            }
            var pluginObj = (AbstractPlugin) defaultConstructor.newInstance(pcl.getPluginInfo(), dataFolder, console);
            var holder = pluginManager.registerPlugin(pluginObj);
            pluginHolders.put(s, holder);
        } catch (ClassNotFoundException e) {
            throw new InvalidPluginInfoException("Main class \"" + main + "\" isn't found for plugin " + name, e, pcl.getPluginFile());
        } catch (NoSuchMethodException e) {
            throw new IllegalPluginException("Can't find the default constructor for the class.", e, pcl.getPluginFile());
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalPluginException("Can't instantitate plugin object for " + name, e, pcl.getPluginFile());
        } catch (IOException e) {
            throw new IllegalStateException("Cannot create datafolder for " + name);
        }
    }

    private void walk(OrderedGraphWalker.ExceptionalConsumer<String> consumer, BiConsumer<String, ? super Exception> exceptions) {
        var walker = new OrderedGraphWalker<>(
                depNetwork, s -> {
            if (presentPlugins.contains(s)) return;
            consumer.accept(s);
        }, exceptions, it -> it == DependencyType.DEPEND);
        walker.walk();
        walker.cleanState();
    }

    private void showCommonError(String name, Exception error) {
        if (error == null) {
            log.warn("Cannot load plugin {} due to one of its dependencies doesn't load", name);
            var holder = pluginHolders.get(name);
            assert holder.getState().getFallbackState() != null;
            holder.setState(holder.getState().getFallbackState());
        } else {
            log.warn("Cannot load plugin {} : {}", name, error);
        }
    }
}
