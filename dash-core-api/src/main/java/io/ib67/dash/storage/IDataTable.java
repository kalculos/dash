package io.ib67.dash.storage;

import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.Optional;

/**
 * Represents a K-V table for data.
 *
 * @param <V>
 */
@ApiStatus.AvailableSince("0.1.0")
public interface IDataTable<K,V> {
    Optional<? extends V> fetchByKey(K key);

    Collection<? extends V> fetchAll();

    boolean containsKey(K key);

    boolean remove(K key);
}
