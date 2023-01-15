package dash.test.event;

import io.ib67.dash.event.AbstractEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class WrapEvent extends AbstractEvent {
    private final AbstractEvent anotherEvent;
}
