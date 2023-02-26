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

package io.ib67.dash.event.bus;

import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannelFactory;
import io.ib67.dash.event.IEventRegistry;
import io.ib67.kiwi.future.Result;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

/**
 * An IEventBus delivers event to its registered handlers.
 */
@ApiStatus.AvailableSince("0.1.0")
public interface IEventBus extends IEventRegistry {
    /**
     * Post an event into the bus.
     * Always call this on where the event is created.
     *
     * @param event the event
     */
    <E extends AbstractEvent> void postEvent(E event, Consumer<Result<E,?>> whenDone);

    /**
     * Gets the channel factory belonging to this bus
     * @return channel factory
     */
    IEventChannelFactory getChannelFactory();
}
