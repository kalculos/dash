package dash.internal.event;

import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.handler.IEventPipeline;
import lombok.Getter;
import lombok.Setter;

import static java.util.Objects.requireNonNull;

public class EventPipeline<E extends AbstractEvent> implements IEventPipeline<E> {
    @Getter
    @Setter
    private boolean cancelled;

    private boolean subscribe;
    private final IEventChannel<E> delegatedChannel;
    private final E event;
    //private final List<Runnable> pendingRegistrations = new ArrayList<>();
    private RegisteredHandler<E> node;

    public EventPipeline(E event, RegisteredHandler<E> node) {
        requireNonNull(this.event = event);
        requireNonNull(this.delegatedChannel = node.channel());
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
            // unsubscribed. (current node)
            // assertion: the 1st node (which is an empty node) never unsubscribe itself.
            node.prev.next = node.next; // remove it from them
        }
        if(node.next != null){
            node = node.next;
            subscribe = true;
            node.handler().handleMessage(this,event);
        }
        // the end of handler list
    }

    @Override
    public IEventChannel<E> channel() {
        return delegatedChannel;
    }
}
