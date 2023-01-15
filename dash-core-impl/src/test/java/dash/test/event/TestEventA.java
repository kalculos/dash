package dash.test.event;

import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.ICancellable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TestEventA extends AbstractEvent implements ICancellable {
    private int i;
    private boolean cancelled=false;

    public TestEventA(int initial){
        i = initial;
    }
}
