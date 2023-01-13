package dash.internal.util;

import java.util.Map;

public interface LongObjectMap<V>
        extends Map<Long, V> {
    V get(long var1);

    V put(long var1, V var3);

    V remove(long var1);

    Iterable<PrimitiveEntry<V>> entries();

    boolean containsKey(long var1);

    interface PrimitiveEntry<V> {
        long key();

        V value();

        void setValue(V var1);
    }
}
