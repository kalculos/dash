package dash.test.event;

import io.ib67.dash.event.AbstractEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class TestEventA extends AbstractEvent {
    private int i;
}
