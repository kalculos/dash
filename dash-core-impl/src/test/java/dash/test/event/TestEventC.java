package dash.test.event;

import io.ib67.dash.event.AbstractEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class TestEventC extends AbstractEvent {
    private int i;
}
