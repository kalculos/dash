package dash.internal.event;

import io.ib67.dash.event.Event;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.ScheduleType;
import io.ib67.dash.event.bus.IEventBus;
import io.ib67.dash.event.handler.EventHandler;
import lombok.RequiredArgsConstructor;

import java.util.TreeSet;
import java.util.concurrent.ExecutorService;

@RequiredArgsConstructor
public class DashEventBus implements IEventBus {
    private final TreeSet<RegisteredHandler<?>> handlers = new TreeSet<>();
    private final ExecutorService executor;

    @Override
    public <E extends Event> void register(IEventChannel<E> channel, EventHandler<E> handler) {
        handlers.add(new RegisteredHandler<>(channel, handler));
    }

    @SuppressWarnings("unchecked")
    public void postEvent(Event event) {
        var iter = handlers.iterator();
        while (iter.hasNext()) {
            var it = iter.next();
            var handler = ((EventHandler<Event>) it.handler());
            if (it.channel().getScheduleType() == ScheduleType.ASYNC) {
                executor.submit(() -> {
                    try {
                        var mappedEvent = it.channel().apply(event);
                        if (mappedEvent == null) {
                            return;
                        }
                        handler.handleMessage(mappedEvent, (IEventChannel<Event>) it.channel());
                    } catch (Throwable throwable) {
                        //todo: log it.
                    }
                });
            } else {
                // assertion: type matches
                try {
                    var mappedEvent = it.channel().apply(event);
                    if (mappedEvent == null) {
                        continue; // doesn't match the condition
                    }
                    var result = handler.handleMessage(mappedEvent, (IEventChannel<Event>) it.channel());
                    switch (result) {
                        case UNSUBSCRIBE -> iter.remove();
                        case CONTINUE -> {
                            continue;
                        }
                        case CANCELLED -> {
                            return;
                        }
                    }
                } catch (Throwable t) {
                    //todo: log it.
                }

            }
        }
    }
}
