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

package io.ib67.dash.message.internal;

import io.ib67.dash.message.ContextKey;
import io.ib67.dash.message.IMessageContext;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@ApiStatus.Internal
public class UnorderedMessageContext implements IMessageContext {
    private final Map<ContextKey<?>, Object> context = new HashMap<>();

    @Override
    public <V> void put(@NotNull ContextKey<V> key, V value) {
        context.put(key, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(@NotNull ContextKey<T> key) {
        return (T) context.get(key);
    }

    @Override
    public boolean has(@NotNull ContextKey<?> key) {
        return context.containsKey(key);
    }

    @Override
    public boolean remove(@NotNull ContextKey<?> key) {
        if(!has(key))return false;
        context.remove(key);
        return true;
    }
}
