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

package io.ib67.dash.context;

import io.ib67.dash.message.internal.UnorderedEventContext;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * A Context stores additional metadata involved in some event, And they are accessible by using a {@link ContextKey}.<br>
 */
@ApiStatus.AvailableSince("0.1.0")
public interface IContext {

    static IContext create() {
        return new UnorderedEventContext();
    }

    /**
     * Put a value indexed by the key into the context.
     * <p>
     * If a null is provided as the value, {@link #has(ContextKey)} will always return false for your key.
     *
     * @param key   index
     * @param value to be stored
     */
    <V> void put(@NotNull ContextKey<V> key, V value);

    /**
     * Get an object from the context. Improper use may lead to {@link ClassCastException}.<br />
     * Example: <code>context.get(KEY);</code>
     *
     * @param key the key
     * @param <T> type of value
     * @return user object or null
     */
    <T> T get(@NotNull ContextKey<T> key);

    /**
     * Check if the context contains something related to the key.
     *
     * @param key key
     * @return if found. Also return false if you set the value to null.
     */
    boolean has(@NotNull ContextKey<?> key);

    /**
     * Remove something from the context.
     *
     * @param key the key
     * @return if there was a key-value pair present.
     */
    default boolean remove(@NotNull ContextKey<?> key) {
        boolean hasPreviously = has(key);
        put(key, null);
        return hasPreviously;
    }

    /**
     * Returns the default value if not found in the context.
     *
     * @param key          key
     * @param defaultValue fallback
     * @param <T>          type of value
     * @return fallback or value
     */
    default <T> T getOrDefault(@NotNull ContextKey<T> key, T defaultValue) {
        if (!has(key)) {
            return defaultValue;
        }
        return get(key);
    }

    /**
     * Generates and puts the value if not found
     *
     * @param key    key
     * @param mapper function
     * @param <T>    type of value
     * @return value
     */
    default <T> T computeIfAbsent(@NotNull ContextKey<T> key, @NotNull Function<ContextKey<T>, T> mapper) {
        if (!has(key)) {
            var r = mapper.apply(key);
            put(key, r);
            return r;
        }
        return get(key);
    }
}
