package dash.internal.event.channels;

import dash.internal.event.AbstractEventChannel;
import io.ib67.dash.event.Event;

import java.util.Objects;
import java.util.function.Predicate;

public class FilteringChannel<E extends Event> extends AbstractEventChannel<E> {
    private final Predicate<? super E> condition;

    public FilteringChannel(AbstractEventChannel<E> parent, Predicate<? super E> condition) {
        super(parent);
        Objects.requireNonNull(this.condition = condition);
    }


    @SuppressWarnings("unchecked") // we don't care its type here.
    protected <B extends Event> B transform(E event) {
        if (event == null) return null;
        if (condition.test(event)) {
            return (B) event;
        }
        return null;
    }
}
