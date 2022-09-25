package dash.internal.event;

import dash.internal.event.channels.FilteringChannel;
import dash.internal.event.channels.MappingChannel;
import io.ib67.dash.event.Event;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.ScheduleType;
import io.ib67.dash.event.bus.IEventBus;
import io.ib67.dash.event.handler.EventHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Predicate;

@ApiStatus.Internal
@Getter
@RequiredArgsConstructor
public abstract class AbstractEventChannel<E extends Event> implements IEventChannel<E> {
    protected final ScheduleType scheduleType;
    protected final String name;
    protected final int priority;
    protected final AbstractEventChannel<?> parent;
    protected final IEventBus bus;

    protected AbstractEventChannel(AbstractEventChannel<E> parent) {
        this.scheduleType = parent.scheduleType;
        this.name = parent.name;
        this.priority = parent.priority;
        this.parent = parent;
        this.bus = parent.bus;
    }

    @Override
    public IEventChannel<E> filter(Predicate<? super E> filter) {
        return new FilteringChannel<>(this, filter);
    }

    @Override
    public <N extends Event> IEventChannel<N> map(Function<? super E, ? extends N> mapper) {
        return new MappingChannel<>(this, mapper);
    }

    @Override
    public @NotNull IEventChannel<E> subscribe(@NotNull EventHandler<E> handler) {
        bus.register(this, handler);
        return this;
    }

    protected abstract <B extends Event> B transform(E event);

    @SuppressWarnings("unchecked") // we don't care its type here.
    @Override
    public Event apply(Event event) {
        if (parent == null) {
            return transform((E) event);
        }
        return transform((E) parent.apply(event));
    }
}
