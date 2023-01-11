package dash.internal.event;

import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.handler.IEventPipeline;
import lombok.Getter;
import lombok.Setter;

import static java.util.Objects.requireNonNull;

public class EventPipeline<E extends AbstractEvent> implements IEventPipeline<E> {
    private static final boolean ALLOW_ASYNC_UNSUBSCRIBE = Boolean.getBoolean("dash.asyncHandlers.mutatePipeline");
    @Getter
    @Setter
    private boolean cancelled;

    private boolean subscribe=true;
    private final E event;
    //private final List<Runnable> pendingRegistrations = new ArrayList<>();
    private RegisteredHandler<E> node;

    public EventPipeline(E event, RegisteredHandler<E> node) {
        requireNonNull(this.event = event);
        requireNonNull(this.node = node);
    }

    @Override
    public void unsubscribe() {
        subscribe = false;
    }

    @Override
    public void fireNext() {
        // check for status
        if (!subscribe) {
            if(!ALLOW_ASYNC_UNSUBSCRIBE){
                throw new IllegalArgumentException("Async handlers cannot be unregistered");
            }
            // unsubscribed. (current node)
            // assertion: the 1st node (which is an empty node) never unsubscribe itself.
            node.prev.next = node.next; // remove it from them
        }
        if(node.next != null){
            node = node.next;
            subscribe = true;
            var handleResult = channel().apply(event);
            if(handleResult == null){
                fireNext();
                return;
            }
            ((RegisteredHandler<AbstractEvent>)node).handler().handleMessage((IEventPipeline<AbstractEvent>) this,handleResult);
        }
        // the end of handler list
    }

    @Override
    public IEventChannel<E> channel() {
        return node.channel();
    }
}
