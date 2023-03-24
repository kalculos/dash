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

import dash.internal.event.channels.FilteringChannel;
import dash.internal.event.channels.MappingChannel;
import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.ScheduleType;
import io.ib67.dash.event.bus.IEventBus;
import io.ib67.dash.event.handler.IEventHandler;
import io.ib67.dash.event.handler.IEventPipeline;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Predicate;

@ApiStatus.Internal
@Getter
@RequiredArgsConstructor
public abstract class AbstractEventChannel<E extends AbstractEvent> implements IEventChannel<E> {
    protected final ScheduleType scheduleType;

    protected final String name;

    protected final int priority;

    protected final AbstractEventChannel<?> parent;

    protected final IEventBus bus;

    protected AbstractEventChannel(AbstractEventChannel<E> parent) {
        this.scheduleType = parent.scheduleType;
        this.name = parent.name;
        this.priority = parent.priority;
        this.parent = parent;
        this.bus = parent.bus;
    }

    @Override
    public IEventChannel<E> filter(Predicate<? super E> filter) {
        return new FilteringChannel<>(this, filter);
    }

    @Override
    public <N extends AbstractEvent> IEventChannel<N> map(Function<? super E, ? extends N> mapper) {
        return new MappingChannel<>(this, mapper);
    }

    @Override
    public @NotNull IEventChannel<E> subscribe(boolean ignoreCancelled, @NotNull IEventHandler<E> handler) {
        bus.register(this, new CancellableHandler(ignoreCancelled, handler));
        return this;
    }

    protected abstract <B extends AbstractEvent> B transform(E event);

    @SuppressWarnings("unchecked") // we don't care its type here.
    @Override
    public AbstractEvent apply(AbstractEvent event) {
        if (parent == null) {
            return transform((E) event);
        }
        return transform((E) parent.apply(event));
    }

    @RequiredArgsConstructor
    private class CancellableHandler implements IEventHandler<E> {
        private final boolean ignoreCancelled;
        private final IEventHandler<E> handler;

        @Override
        public void handleMessage(@NotNull IEventPipeline<E> pipeline, E event) {
            if (ignoreCancelled && event.isCancelled()) {
                pipeline.fireNext();
                return;
            }
            handler.handleMessage(pipeline, event);
        }
    }
}
