package dash.internal.event;

import io.ib67.dash.event.Event;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.handler.EventHandler;
import org.jetbrains.annotations.NotNull;

record RegisteredHandler<E extends Event>(
        @NotNull IEventChannel<E> channel,
        @NotNull EventHandler<E> handler,
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
