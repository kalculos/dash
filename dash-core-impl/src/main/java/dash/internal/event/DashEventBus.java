/*
 * MIT License
 *
 * Copyright (c) 2023 Kalculos and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dash.internal.event;

import dash.internal.registry.SimpleEventRegistry;
import dash.internal.util.Threads;
import io.ib67.dash.event.*;
import io.ib67.dash.event.bus.IEventBus;
import io.ib67.dash.event.handler.IEventHandler;
import io.ib67.kiwi.Kiwi;
import io.ib67.kiwi.future.Future;
import io.ib67.kiwi.future.Result;
import io.ib67.kiwi.future.TaskPromise;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

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
    public void registerListeners(EventListener listener) {
        delegatedRegistry.registerListeners( listener);
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
    public <E extends AbstractEvent> Future<E, ?> postEvent(E event) {
        var result = deliverEvent(MONITOR, event);
        if (result.isFailed()) {
            return result;
        }
        if (Threads.isPrimaryThread()) {
            result = deliverEvent(MAIN, event);
            if (handlers.containsKey(ASYNC))
                asyncExecutor.submit(() -> deliverEvent(ASYNC, event));
            return result;
        } else {
            Future<E,Object> promise = result;
            if (handlers.containsKey(MAIN)) {
                var _promise = new TaskPromise<E,Object>();
                mainExecutor.submit(() -> {
                    _promise.fromResult(deliverEvent(MAIN, event));
                });
                promise = _promise;
            }
            if (handlers.containsKey(ASYNC))
                mainExecutor.submit(() -> asyncExecutor.submit(() -> deliverEvent(ASYNC, event)));
            return promise;
        }
    }

    @SuppressWarnings("unchecked")
    private <E extends AbstractEvent, T> Result<E, T> deliverEvent(ScheduleType type, E event) {
        if (!handlers.containsKey(type)) {
            return Result.ok(event);
        }
        var node = handlers.get(type);
        var pipeline = new EventPipeline<>(event, (RegisteredHandler<E>) node,channelFactory);
        var result = Kiwi.fromAny(()->{
            pipeline.fireNext();
            return event;
        });
        return (Result<E, T>) result;
    }
}
