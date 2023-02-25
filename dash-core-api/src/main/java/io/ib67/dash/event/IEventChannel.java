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

package io.ib67.dash.event;

import io.ib67.dash.event.bus.IEventBus;
import io.ib67.dash.event.handler.IEventHandler;
import io.ib67.dash.event.handler.IEventPipeline;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * An EventChannel is a {@link java.util.stream.Stream} variant that designed for receiving events. <br />
 * For more about Events, please refer to {@link AbstractEvent}
 * For more about where your handlers will be called, please refer to {@link ScheduleType}
 */
@ApiStatus.AvailableSince("0.1.0")
@SuppressWarnings("unused")
public interface IEventChannel<E extends AbstractEvent> extends Comparable<IEventChannel<E>>, UnaryOperator<AbstractEvent> {
    /**
     * The default priority for all IEventChannels.
     */
    int DEFAULT_PRIORITY = EventPriorities.NORMAL;

    /**
     * The {@link ScheduleType} for this EventChannel.
     *
     * @return the schedulerType
     */
    @NotNull
    ScheduleType getScheduleType();

    /**
     * User-defined name of this EventChannel.
     * Can be null if not defined.
     *
     * @return nullable name, should only for debugging purposes.
     */
    @Nullable
    String getName();

    IEventBus getBus();

    /**
     * The priority of this event channel.
     *
     * @return priority. Smaller is earlier. Must be positive
     */
    int getPriority();

    @Override
    default int compareTo(@NotNull IEventChannel<E> o) {
        if (o.getScheduleType() != getScheduleType()) {
            return o.getScheduleType().compareTo(this.getScheduleType());
        }
        return Integer.compare(getPriority(), o.getPriority());
    }

    /**
     * Split this EventChannel for a condition.
     *
     * @param filter condition
     * @return new channel
     */
    @Contract("_ -> new")
    IEventChannel<E> filter(Predicate<? super E> filter);

    /**
     * A `type filter` variant of {@link #map(Function)}
     *
     * @param type target type
     * @param <P>  new type
     * @return a new event channel
     */
    default <P extends AbstractEvent> IEventChannel<P> filterForType(Class<P> type) {
        return filter(type::isInstance).map(type::cast);
    }

    /**
     * Split this EventChannel for a new type.
     * This is possible to cause {@link ClassCastException}. For finding specified types, please use {@link #filterForType(Class)}
     *
     * @param mapper type mapper
     * @param <N>    new event type
     * @return new channel
     */
    @Contract("_ -> new")
    <N extends AbstractEvent> IEventChannel<N> map(Function<? super E, ? extends N> mapper);

    /**
     * Subscribes to this EventChannel. Your handler will receive messages until it call {@link IEventPipeline#unsubscribe()}
     *
     * @param handler subscriber
     * @return this
     */
    @NotNull IEventChannel<E> subscribe(boolean ignoreCancelled, @NotNull IEventHandler<E> handler);

    default IEventChannel<E> subscribeAlways(@NotNull IEventHandler<E> subscriber) {
        return this.subscribe(true, subscriber);
    }

    default IEventChannel<E> subscribeOnce(@NotNull IEventHandler<E> subscriber) {
        return this.subscribe(true, (pipeline, event) -> {
            pipeline.unsubscribe();
            subscriber.handleMessage(pipeline,event);
        });
    }
}
