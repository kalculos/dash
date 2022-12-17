package dash.internal.event;

import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.handler.IEventHandler;
import org.jetbrains.annotations.NotNull;

record RegisteredHandler<E extends AbstractEvent>(
        @NotNull IEventChannel<E> channel,
        @NotNull IEventHandler<E> handler,
        int increasingCounter // to allow same priority in TreeSet.
) implements Comparable<RegisteredHandler<?>> {
    @Override
    @SuppressWarnings("unchecked")
    public int compareTo(@NotNull RegisteredHandler o) {
        int delta = channel.compareTo(o.channel);
        if (delta != 0) return delta;
        return increasingCounter - o.increasingCounter;
    }
}
