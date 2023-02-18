package io.ib67.dash.message;

import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.requireNonNull;

/**
 * ContextKeys are used to index a value in a context from {@link AbstractMessage}. They are immutable and shared between instances.<br />
 * Also see {@link IMessageContext}
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
}
