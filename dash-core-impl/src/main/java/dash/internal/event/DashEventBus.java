package dash.internal.event;

import dash.internal.util.Threads;
import io.ib67.dash.event.Event;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.ScheduleType;
import io.ib67.dash.event.bus.IEventBus;
import io.ib67.dash.event.handler.EventHandler;
import io.ib67.dash.event.handler.HandleResult;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static io.ib67.dash.event.ScheduleType.*;
import static java.util.Objects.requireNonNull;

public class DashEventBus implements IEventBus {
    private final Map<ScheduleType, SortedSet<RegisteredHandler<?>>> handlers = new EnumMap<>(ScheduleType.class);
    private final ExecutorService asyncExecutor;
    private final ScheduledExecutorService mainExecutor;
    private final AtomicInteger handlerCounter = new AtomicInteger(0);

    public DashEventBus(ExecutorService asyncExecutor, ScheduledExecutorService mainExecutor) {
        requireNonNull(this.asyncExecutor = asyncExecutor);
        requireNonNull(this.mainExecutor = mainExecutor);
        mainExecutor.execute(() -> {
            Threads.primaryThread = Thread.currentThread();
        });
    }


    @Nullable
    @SuppressWarnings("unchecked")
    private static <E extends Event> HandleResult getHandleResult(E event, RegisteredHandler<?> it) {
        var evt = it.channel().apply(event);
        if (evt == null) return null;
        return ((EventHandler<Event>) it.handler()).handleMessage(evt, (IEventChannel<Event>) it.channel());
    }

    @Override
    public <E extends Event> void register(IEventChannel<E> channel, EventHandler<E> handler) {
        requireNonNull(channel.getScheduleType());
        handlers.computeIfAbsent(channel.getScheduleType(), k -> new TreeSet<>())
                .add(new RegisteredHandler<>(channel, handler, handlerCounter.addAndGet(1)));
    }

    public <E extends Event> void postEvent(E event, Consumer<E> whenDone) {
        postEvent1(event, MONITOR);
        if (!Threads.isPrimaryThread()) {
            // don't post if nothing subscribe on main
            if (handlers.containsKey(MAIN)) {
                mainExecutor.submit(() -> postEvent0(event, whenDone));
            }
        } else {
            postEvent0(event, whenDone);
        }
    }

    private <E extends Event> void postEvent0(E event, Consumer<E> whenDone) {
        postEvent1(event, MAIN);
        postEvent1(event, ASYNC);
        whenDone.accept(event);
    }

    private <E extends Event> void postEvent1(E event, ScheduleType type) {
        var _handlers = handlers.getOrDefault(type, Collections.emptySortedSet());
        var iter = _handlers.iterator();
        while (iter.hasNext()) {
            var it = iter.next();
            if (type == ASYNC) {
                asyncExecutor.submit(() -> getHandleResult(event, it));
                continue;
            }
            var result = getHandleResult(event, it);
            if (result == null) continue;
            switch (result) {
                case CONTINUE -> {
                    // do nothing.
                }
                case CANCELLED -> {
                    return;
                }
                case UNSUBSCRIBE -> iter.remove();
                default -> {
                }
            }
        }
    }
}
