package dash.test.event;

import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.ICancellable;
import lombok.Getter;
import lombok.Setter;

public class CancellableEvent extends AbstractEvent implements ICancellable {
    @Setter
    @Getter
    private boolean cancelled;
}
