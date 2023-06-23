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

import lombok.SneakyThrows;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigHolder<T> {
    private ConfigurationNode moduleConfigRoot;
    private ConfigurationLoader<?> moduleConfigLoader;
    private final Path confFile;
    private final Class<T> configClass;

    @SneakyThrows
    public ConfigHolder(Path confFile, ConfigurationOptions opt, Class<T> configClass) {
        this.confFile = confFile;
        this.configClass = configClass;
        this.moduleConfigLoader = YamlConfigurationLoader.builder()
                .path(confFile)
                .defaultOptions(opt)
                .build();
        if(Files.notExists(confFile)){
            var root = moduleConfigLoader.load();
            root.set(initializeConfigPojo(configClass));
            moduleConfigRoot = root;
            save();
        }else{
            moduleConfigRoot = moduleConfigLoader.load();
        }
    }

    @SneakyThrows
    public void save() {
        moduleConfigLoader.save(moduleConfigRoot);
    }

    @SneakyThrows
    public T getConfig() {
        return moduleConfigRoot.get(configClass);
    }

    @SneakyThrows
    public void reload() {
        this.moduleConfigLoader = YamlConfigurationLoader.builder()
                .path(confFile)
                .build();
        moduleConfigRoot = moduleConfigLoader.load();
    }

    @SneakyThrows
    public void saveConfig(Object config) {
        moduleConfigRoot.set(config);
        save();
    }
    private static <A> A initializeConfigPojo(Class<A> configClazz) throws IllegalAccessException {
        try {
            for (Method declaredMethod : configClazz.getDeclaredMethods()) {
                if (declaredMethod.isAnnotationPresent(DefaultConfigConstructor.class)) {
                    declaredMethod.setAccessible(true);
                    return configClazz.cast(declaredMethod.invoke(null));
                }
            }
            throw new IllegalArgumentException("Can't find a default instance for " + configClazz);
        } catch (InvocationTargetException targetException) {
            throw new IllegalAccessException("Can't construct a default config: " + targetException.getMessage());
        }
    }
}
