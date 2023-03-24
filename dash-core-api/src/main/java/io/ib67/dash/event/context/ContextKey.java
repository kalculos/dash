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

package io.ib67.dash.event.context;

import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.requireNonNull;

/**
 * ContextKeys are used to index a value in a context from {@link io.ib67.dash.event.ContextualEvent}. They are immutable and shared between instances.<br />
 * Also see {@link IEventContext}
 * @param <T> the type of the value index by this key, only for type-safe checks.
 */
@ToString
public class ContextKey<T> {
    private static final Map<String, ContextKey<?>> keys = new ConcurrentHashMap<>();
    private static final AtomicInteger CURRENT_INDEX = new AtomicInteger();
    @Getter
    private final String name;
    @Getter
    private final int index;

    public ContextKey(String s, int index) {
        this.index = index;
        requireNonNull(name = s);
    }

    @SuppressWarnings("unchecked")
    public static <T> ContextKey<T> of(String key) {
        return (ContextKey<T>) keys.computeIfAbsent(key.toLowerCase(), s -> new ContextKey<>(s, CURRENT_INDEX.getAndIncrement()));
    }

    public static int getCurrentIndex(){
        return CURRENT_INDEX.get();
    }

    @Override
    public int hashCode() {
        return index;
    }
}
