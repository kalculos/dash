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

package dash.internal.event;

import dash.internal.event.channels.AcceptingChannel;
import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.ScheduleType;
import io.ib67.dash.event.handler.IEventHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class RegisteredHandler<E extends AbstractEvent> implements Comparable<RegisteredHandler<?>> {
    private final @NotNull IEventChannel<E> channel;
    private final @NotNull IEventHandler<E> handler;
    @Nullable RegisteredHandler<E> next;
    @Nullable RegisteredHandler<E> prev;

    RegisteredHandler(
            @NotNull IEventChannel<E> channel,
            @NotNull IEventHandler<E> handler
    ) {
        this.channel = channel;
        this.handler = handler;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int compareTo(@NotNull RegisteredHandler o) {
        return channel.compareTo(o.channel);
    }

    @SuppressWarnings("unchecked")
    public void insertSorted(RegisteredHandler handler) {
        var delta = compareTo(handler);
        if (delta == 0) {
            insertAfter(handler);
            return;
        } else if (delta < 0) {
            if (next != null) {
                next.insertSorted(handler);
            }else{
                next = handler;
                handler.prev = this; // append
            }
            return;
        }
        // this > handler
        insertBefore(handler);
    }

    @SuppressWarnings("unchecked")
    private void insertBefore(RegisteredHandler handler) {
        next = handler.next;
        handler.next = this;
        handler.prev = prev;
        prev = handler;
    }

    @SuppressWarnings("unchecked")
    private void insertAfter(RegisteredHandler handler) {
        if (next != null) next.prev = handler;
        handler.next = next;
        handler.prev = this;
        next = handler;
    }

    public static <E extends AbstractEvent> RegisteredHandler<E> createEmpty(ScheduleType defaultScheduleType) {
        return new RegisteredHandler(
                new AcceptingChannel(defaultScheduleType, null, 0, null, null),
                (pipeline, event) -> pipeline.fireNext()
        );
    }

    public @NotNull IEventChannel<E> channel() {
        return channel;
    }

    public @NotNull IEventHandler<E> handler() {
        return handler;
    }


    @Override
    public String toString() {
        return "RegisteredHandler[" +
                "channel=" + channel + ", " +
                "handler=" + handler + ']';
    }

}
