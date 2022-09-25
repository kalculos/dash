package dash.internal.event;

import io.ib67.dash.event.Event;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.handler.EventHandler;
import org.jetbrains.annotations.NotNull;

record RegisteredHandler<E extends Event>(
        @NotNull IEventChannel<E> channel,
        @NotNull EventHandler<E> handler
) implements Comparable<RegisteredHandler<?>> {
    @Override
    @SuppressWarnings("unchecked")
    public int compareTo(@NotNull RegisteredHandler o) {
        return channel.compareTo(o.channel);
    }
}
