package io.ib67.dash.event;

import io.ib67.dash.event.handler.EventHandler;
import io.ib67.dash.event.handler.HandleResult;
import io.ib67.dash.event.handler.internal.CatchyHandler;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * An EventChannel is a {@link java.util.stream.Stream} variant that designed for receiving events. <br />
 * For more about Events, please refer to {@link Event}
 * For more about where your handlers will be called, please refer to {@link ScheduleType}
 */
@ApiStatus.AvailableSince("0.1.0")
@SuppressWarnings("unused")
public interface IEventChannel<E extends Event> extends Comparable<IEventChannel<E>>, UnaryOperator<Event> {
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
        return getPriority() - o.getPriority();
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
    default <P extends Event> IEventChannel<P> filterForType(Class<P> type) {
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
    <N extends Event> IEventChannel<N> map(Function<? super E, ? extends N> mapper);

    /**
     * Subscribes to this EventChannel. Your handler will receive messages until it returns {@link HandleResult#UNSUBSCRIBE}
     *
     * @param handler subscriber
     * @return this
     */
    @NotNull IEventChannel<E> subscribe(@NotNull EventHandler<E> handler);

    default IEventChannel<E> subscribeAlways(@NotNull BiConsumer<Event, IEventChannel<E>> subscriber) {
        return this.subscribe((event, channel) -> {
            subscriber.accept(event, channel);
            return HandleResult.CONTINUE;
        });
    }

    default IEventChannel<E> subscribeOnce(@NotNull BiConsumer<Event, IEventChannel<E>> subscriber) {
        return this.subscribe((event, channel) -> {
            subscriber.accept(event, channel);
            return HandleResult.UNSUBSCRIBE;
        });
    }

    default IEventChannel<E> subscribeUntil(@NotNull Predicate<Event> shouldContinue, @NotNull BiConsumer<Event, IEventChannel<E>> subscriber) {
        return this.subscribe((event, channel) -> {
            subscriber.accept(event, channel);
            return shouldContinue.test(event) ? HandleResult.CONTINUE : HandleResult.UNSUBSCRIBE;
        });
    }

    default IEventChannel<E> subscribeCatchy(@NotNull BiConsumer<Event, IEventChannel<E>> subscriber) {
        if (!getScheduleType().isOrdered())
            throw new IllegalStateException("subscribeCatchy doesn't support ASYNC EventChannels");
        return this.subscribe(new CatchyHandler<>(subscriber));
    }
}
