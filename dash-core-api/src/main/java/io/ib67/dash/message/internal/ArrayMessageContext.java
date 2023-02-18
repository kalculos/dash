package io.ib67.dash.message.internal;

import io.ib67.dash.message.ContextKey;
import io.ib67.dash.message.IMessageContext;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@ApiStatus.Internal
public class ArrayMessageContext implements IMessageContext {
    private volatile Object[] context = new Object[calcFitLen()];

    private int calcFitLen() {
        //Find the closest power of 2 to the current number
        int newCapacity = ContextKey.getCurrentIndex();
        newCapacity |= newCapacity >>> 1;
        newCapacity |= newCapacity >>> 2;
        newCapacity |= newCapacity >>> 4;
        newCapacity |= newCapacity >>> 8;
        newCapacity |= newCapacity >>> 16;
        newCapacity++;
        return newCapacity;
    }

    private synchronized void resize(int lowest) {
        if (shouldResize(lowest)) return;
        context = Arrays.copyOf(context, calcFitLen());
    }

    private boolean shouldResize(int lowest) {
        return context.length - 1 < lowest;
    }

    @Override
    public void put(@NotNull ContextKey key, Object value) {
        if (shouldResize(key.getIndex())) resize(key.getIndex());
        context[key.getIndex()] = value;
    }

    @Override
    @SuppressWarnings("unchecked") // user took the responsibility to handle CCEs.
    public <T> T get(@NotNull ContextKey key) {
        if (shouldResize(key.getIndex())) { // the index is bigger than the array
            return null;
        }
        return (T) context[key.getIndex()];
    }

    @Override
    public boolean has(@NotNull ContextKey key) {
        return get(key) != null;
    }
}
