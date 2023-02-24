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
}
