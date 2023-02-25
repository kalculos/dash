package dash.internal.event;

import dash.internal.registry.SimpleEventRegistry;
import dash.internal.util.Threads;
import io.ib67.dash.AbstractBot;
import io.ib67.dash.event.*;
import io.ib67.dash.event.bus.IEventBus;
import io.ib67.dash.event.handler.IEventHandler;
import io.ib67.kiwi.Kiwi;
import io.ib67.kiwi.Result;
import lombok.Getter;

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

    @Getter
    private final IEventChannelFactory channelFactory;
    private final IEventRegistry delegatedRegistry;

    public DashEventBus(ScheduledExecutorService mainLoop, ExecutorService asyncLoop) {
        requireNonNull(this.mainExecutor = mainLoop);
        requireNonNull(this.asyncExecutor = asyncLoop);
        channelFactory = new SimpleEventChannelFactory(this);
        delegatedRegistry = new SimpleEventRegistry(channelFactory);
    }

    @Override
    public void registerListeners(AbstractBot bot, EventListener listener) {
        delegatedRegistry.registerListeners(bot, listener);
    }

    @Override
    public <E extends AbstractEvent> void register(IEventChannel<E> channel, IEventHandler<E> handler) {
        if (!Threads.isPrimaryThread()) {
            mainExecutor.submit(() -> register(channel, handler));
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
    public <E extends AbstractEvent> void postEvent(E event, Consumer<Result<E, ?>> whenDone) {
        var result = deliverEvent(MONITOR, event);
        if (result.isFailed()) {
            whenDone.accept(result);
            return;
        }
        if (Threads.isPrimaryThread()) {
            whenDone.accept(deliverEvent(MAIN, event));
        } else {
            if (handlers.containsKey(MAIN)) {
                mainExecutor.submit(() -> {
                    whenDone.accept(deliverEvent(MAIN, event));
                });
            } else {
                whenDone.accept(result);
            }
        }
        if (handlers.containsKey(ASYNC))
            mainExecutor.submit(() -> asyncExecutor.submit(() -> deliverEvent(ASYNC, event)));
    }

    @SuppressWarnings("unchecked")
    private <E extends AbstractEvent> Result<E, ?> deliverEvent(ScheduleType type, E event) {
        if (!handlers.containsKey(type)) {
            return Result.ok(event);
        }
        var node = handlers.get(type);
        var pipeline = new EventPipeline<>(event, (RegisteredHandler<E>) node);
        var result = Kiwi.runAny(pipeline::fireNext);
        return result.isFailed() ? (Result<E, ?>) result : Result.ok(event);
    }
}
