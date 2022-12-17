package dash.internal.event.channels;

import dash.internal.event.AbstractEventChannel;
import io.ib67.dash.event.AbstractEvent;

import java.util.Objects;
import java.util.function.Function;

public class MappingChannel<IN extends AbstractEvent, OUT extends AbstractEvent> extends AbstractEventChannel<IN> {
    private final Function<? super IN, ? extends OUT> mapper;

    @SuppressWarnings("unchecked") //todo fuck generics
    public MappingChannel(Object parent, Function mapper) {
        super((AbstractEventChannel<IN>) parent);
        Objects.requireNonNull(this.mapper = mapper);
    }


    @SuppressWarnings("unchecked") // we don't care its type here.
    @Override
    protected <B extends AbstractEvent> B transform(IN event) {
        if (event == null) return null;
        return (B) mapper.apply(event);
    }
}
