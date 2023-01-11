package dash.internal.event.channels;

import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.ScheduleType;
import io.ib67.dash.event.bus.IEventBus;
import io.ib67.dash.event.handler.IEventHandler;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class DelegatedEventChannel<E extends AbstractEvent> implements IEventChannel<E> {
    private final IEventChannel<E> channel;
    private final List<Runnable> queue;
    @Override
    public @NotNull ScheduleType getScheduleType() {
        return channel.getScheduleType();
    }

    @Override
    public @Nullable String getName() {
        return channel.getName();
    }

    @Override
    public IEventBus getBus() {
        return channel.getBus();
    }

    @Override
    public int getPriority() {
        return channel.getPriority();
    }

    @Override
    public IEventChannel<E> filter(Predicate<? super E> filter) {
        return new DelegatedEventChannel<>(channel.filter(filter),queue);
    }

    @Override
    public <N extends AbstractEvent> IEventChannel<N> map(Function<? super E, ? extends N> mapper) {
        return new DelegatedEventChannel<>(channel.map(mapper),queue);
    }

    @Override
    public @NotNull IEventChannel<E> subscribe(boolean ignoreCancelled, @NotNull IEventHandler<E> handler) {
        queue.add(()->channel.subscribe(ignoreCancelled, handler));
        return this;
    }

    @Override
    public AbstractEvent apply(AbstractEvent abstractEvent) {
        return channel.apply(abstractEvent);
    }
}
