package dash.internal.event;

import dash.internal.util.Threads;
import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.ScheduleType;
import io.ib67.dash.event.bus.IEventBus;
import io.ib67.dash.event.handler.IEventHandler;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

import static io.ib67.dash.event.ScheduleType.*;
import static java.util.Objects.requireNonNull;

public class DashEventBus implements IEventBus {
    private final Map<ScheduleType, RegisteredHandler<?>> handlers = new EnumMap<>(ScheduleType.class);

    private final ExecutorService asyncExecutor;

    private final ScheduledExecutorService mainExecutor;

    public DashEventBus(ScheduledExecutorService mainLoop, ExecutorService asyncLoop) {
        requireNonNull(this.mainExecutor = mainLoop);
        requireNonNull(this.asyncExecutor = asyncLoop);
    }

    @Override
    public <E extends AbstractEvent> void register(IEventChannel<E> channel, IEventHandler<E> handler) {
        if(!Threads.isPrimaryThread()){
            mainExecutor.submit(()->register(channel, handler));
            return;
        }
        if (!handlers.containsKey(channel.getScheduleType())) {
            var _handler = RegisteredHandler.createEmpty(channel.getScheduleType());
            _handler.insertSorted(new RegisteredHandler<>(channel, handler));
            handlers.put(channel.getScheduleType(), _handler);
        } else {
            var node = handlers.get(channel.getScheduleType());
            node.insertSorted(new RegisteredHandler<>(channel, handler));
        }
    }

    @Override
    public <E extends AbstractEvent> void postEvent(E event, Consumer<E> whenDone) {
        deliverEvent(MONITOR, event);
        if (Threads.isPrimaryThread()) {
            deliverEvent(MAIN, event);
        } else {
            if (handlers.containsKey(MAIN)) mainExecutor.submit(() -> deliverEvent(MAIN, event));
        }
        if (handlers.containsKey(ASYNC))
            mainExecutor.submit(() -> asyncExecutor.submit(() -> deliverEvent(ASYNC, event)));
    }

    @SuppressWarnings("unchecked")
    private <E extends AbstractEvent> void deliverEvent(ScheduleType type, E event) {
        if (!handlers.containsKey(type)) {
            return;
        }
        var node = handlers.get(type);
        var pipeline = new EventPipeline<>(event, (RegisteredHandler<E>) node);
        pipeline.fireNext();
    }
}
