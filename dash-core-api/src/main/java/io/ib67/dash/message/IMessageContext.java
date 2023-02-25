package io.ib67.dash.message;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * A MessageContext stores additional metadata involved in a {@link AbstractMessage}, And they are accessible with {@link ContextKey}.<br>
 * Getting or putting data from MessageContext is very fast, however you'll have to waste some memory when you have a large amount of context keys.<br>
 * Usually you don't need to care about that memory issue. An Object[128] is actually small if compared to your business objects.
 */
public interface IMessageContext {

    /**
     * Put a value indexed by the key into the context.
     *
     * If a null is provided as the value, {@link #has(ContextKey)} will always return false for your key.
     * @param key index
     * @param value to be stored
     */
    <V> void put(@NotNull ContextKey<V> key, V value);

    /**
     * Get an object from the context. Improper use may lead to {@link ClassCastException}.<br />
     * Example: <code>context.get(KEY);</code>
     * @param key the key
     * @return user object or null
     * @param <T> type of value
     */
    <T> T get(@NotNull ContextKey<T> key);

    /**
     * Check if the context contains something related to the key.
     * @param key key
     * @return if found. Also return false if you set the value to null.
     */
    boolean has(@NotNull ContextKey<?> key);

    /**
     * Remove something from the context.
     * @param key the key
     * @return if there was a key-value pair present.
     */
    default boolean remove(@NotNull ContextKey<?> key){
        boolean hasPreviously = has(key);
        put(key,null);
        return hasPreviously;
    }

    /**
     * Returns the default value if not found in the context.
     * @param key key
     * @param defaultValue fallback
     * @return fallback or value
     * @param <T> type of value
     */
    default <T> T getOrDefault(@NotNull ContextKey<T> key, T defaultValue){
        if(!has(key)){
            return defaultValue;
        }
        return get(key);
    }

    /**
     * Generates and puts the value if not found
     * @param key key
     * @param mapper function
     * @return value
     * @param <T> type of value
     */
    default <T> T computeIfAbsent(@NotNull ContextKey<T> key, @NotNull Function<ContextKey<T>,T> mapper){
        if(!has(key)){
            var r = mapper.apply(key);
            put(key,r);
            return r;
        }
        return get(key);
    }
}
