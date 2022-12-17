package dash.internal.event.channels;

import dash.internal.event.AbstractEventChannel;
import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.ScheduleType;
import io.ib67.dash.event.bus.IEventBus;

public class AcceptingChannel<E extends AbstractEvent> extends AbstractEventChannel<E> {
    public AcceptingChannel(ScheduleType scheduleType, String name, int priority, AbstractEventChannel<?> parent, IEventBus bus) {
        super(scheduleType, name, priority, parent, bus);
    }

    @Override
    protected <B extends AbstractEvent> B transform(E event) {
        return (B) event;
    }
}
