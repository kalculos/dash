package dash.internal.event;

import dash.internal.event.channels.AcceptingChannel;
import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.IEventChannelFactory;
import io.ib67.dash.event.ScheduleType;
import io.ib67.dash.event.bus.IEventBus;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class SimpleEventChannelFactory implements IEventChannelFactory {
    private final IEventBus bus;

    @Override
    public <E extends AbstractEvent> IEventChannel<E> from(ScheduleType scheduleType, @Nullable String name, int priority) {
        return new AcceptingChannel<>(
                scheduleType,name,priority,null,bus
        );
    }
}
