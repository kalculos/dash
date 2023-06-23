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

package io.ib67.dash.console.util.config;

import io.ib67.dash.console.plugin.AbstractPlugin;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.configurate.ConfigurationOptions;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * A utility to load configurations. Use it with {@link PluginConfig}.
 */
@ApiStatus.AvailableSince("0.1.0")
@Slf4j
public class ConfigLoader {
    private final AbstractPlugin plugin;
    private final ConfigurationOptions confOpt;
    private final Map<Field, ConfigHolder<?>> heldConfigs = new HashMap<>();

    public ConfigLoader(AbstractPlugin plugin, ConfigurationOptions options) {
        this.plugin = requireNonNull(plugin, "Plugin cannot be null");
        this.confOpt = options == null ? ConfigurationOptions.defaults() : options;
    }

    /**
     * Load all configurations by annotated fields in your plugin instance.
     * Fail-fast.
     *
     * @throws IOException if any configuration is failed to load.
     */
    public void load() throws IOException {
        var dataFolder = plugin.getDataFolder().toAbsolutePath();
        if (Files.notExists(dataFolder)) {
            Files.createDirectories(dataFolder);
        }
        // fetch configs
        for (Field declaredField : plugin.getClass().getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(PluginConfig.class)) {
                var annotation = declaredField.getAnnotation(PluginConfig.class);
                var file = dataFolder.resolve(annotation.fileName());
                heldConfigs.put(declaredField, new ConfigHolder<>(file, confOpt, declaredField.getDeclaringClass()));
            }
        }
        heldConfigs.forEach(this::applyValue);
    }

    @SneakyThrows
    private void applyValue(Field field, ConfigHolder<?> configHolder) {
        field.setAccessible(true);
        field.set(plugin, configHolder.getConfig());
    }

    public void saveAll() throws IOException {
        var dataFolder = plugin.getDataFolder().toAbsolutePath();
        if (Files.notExists(dataFolder)) {
            Files.createDirectories(dataFolder);
        }
        heldConfigs.values().forEach(ConfigHolder::save);
    }
}
