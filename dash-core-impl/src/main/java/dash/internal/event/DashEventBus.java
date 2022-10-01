package dash.internal.event;

import dash.internal.util.Threads;
import io.ib67.dash.event.Event;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.bus.IEventBus;
import io.ib67.dash.event.handler.EventHandler;
import io.ib67.dash.scheduler.Scheduler;
import lombok.RequiredArgsConstructor;

import java.util.TreeSet;
import java.util.concurrent.ExecutorService;

import static io.ib67.dash.event.ScheduleType.ASYNC;
import static io.ib67.dash.event.ScheduleType.MONITOR;

@RequiredArgsConstructor
public class DashEventBus implements IEventBus {
    private final TreeSet<RegisteredHandler<?>> handlers = new TreeSet<>();
    private final TreeSet<RegisteredHandler<?>> monitors = new TreeSet<>();
    private final ExecutorService asyncExecutor;
    private final Scheduler mainExecutor;

    @Override
    public <E extends Event> void register(IEventChannel<E> channel, EventHandler<E> handler) {
        if (channel.getScheduleType() == MONITOR) {
            monitors.add(new RegisteredHandler<>(channel, handler));
            return;
        }
        handlers.add(new RegisteredHandler<>(channel, handler));
    }

    @SuppressWarnings("unchecked")
    public void postEvent(Event event) { //todo: try to clean duplicated codes
        tickMonitor:
        {
            var iter = monitors.iterator();
            while (iter.hasNext()) {
                var it = iter.next();
                var handler = ((EventHandler<Event>) it.handler());
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
        if (!Threads.isPrimaryThread()) {
            mainExecutor.add(() -> postEvent(event)).onlyFor(1);
            return;
        }
        var iter = handlers.iterator();
        while (iter.hasNext()) {
            var it = iter.next();
            var handler = ((EventHandler<Event>) it.handler());
            if (it.channel().getScheduleType() == ASYNC) {
                asyncExecutor.submit(() -> {
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
                return;
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
