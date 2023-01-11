package dash.internal.event;

import dash.internal.event.channels.AcceptingChannel;
import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.handler.IEventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

final class RegisteredHandler<E extends AbstractEvent> implements Comparable<RegisteredHandler<?>> {
    private final @NotNull IEventChannel<E> channel;
    private final @NotNull IEventHandler<E> handler;
    RegisteredHandler<E> next;
    RegisteredHandler<E> prev;

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
        if (delta == 0) { // same
            handler.next = next;
            this.next = handler.next;
            handler.prev = this;
        } else if (delta < 0) { // 1 2  <- 2
            if (next == null) {
                next = handler; // 1 2 [2], append
                handler.prev = this;
                return;
            }
            if (next.compareTo(handler) <= 0) {
                next.insertSorted(handler);
            } else { // 1 [2] 3
                handler.next = next;
                this.next = handler.next;
                handler.prev = this;
            }
        } else { // delta > 0
            throw new IllegalStateException("impossible");
        }
    }

    public static <E extends AbstractEvent> RegisteredHandler<E> createEmpty(){
        return new RegisteredHandler(
                new AcceptingChannel(null,null,0,null,null),
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
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (RegisteredHandler) obj;
        return Objects.equals(this.channel, that.channel) &&
                Objects.equals(this.handler, that.handler);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channel, handler);
    }

    @Override
    public String toString() {
        return "RegisteredHandler[" +
                "channel=" + channel + ", " +
                "handler=" + handler + ']';
    }

}
