package io.ib67.dash.message;

import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.requireNonNull;

/**
 * ContextKeys are used to index a value in a context from {@link AbstractMessage}. They are immutable and shared between instances.
 */
@ToString
public class ContextKey {
    private static final Map<String, ContextKey> keys = new ConcurrentHashMap<>();
    private static final AtomicInteger CURRENT_INDEX = new AtomicInteger();
    @Getter
    private final String name;
    @Getter
    private final int index;

    public ContextKey(String s, int index) {
        this.index = index;
        requireNonNull(name = s.toLowerCase());
    }

    public static ContextKey of(String key) {
        return keys.computeIfAbsent(key, s -> new ContextKey(s, CURRENT_INDEX.getAndIncrement()));
    }

    public static int getCurrentIndex(){
        return CURRENT_INDEX.get();
    }
}
