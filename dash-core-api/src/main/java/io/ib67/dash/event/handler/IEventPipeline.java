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

package io.ib67.dash.event.handler;

import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;
import org.jetbrains.annotations.ApiStatus;

/**
 * An {@link IEventPipeline} represents handlers for an event. {@link IEventHandler}s uses this to affect event delivery and register more handlers.
 *
 * @param <E>
 */
@ApiStatus.AvailableSince("0.1.0")
public interface IEventPipeline<E extends AbstractEvent> {

    /**
     * Deregister this handler .
     */
    void unsubscribe();

    /**
     * Passes this to next handler.
     */
    void fireNext();

    /**
     * Registers a new event handler, which is guaranteed to be called earlier than current handler.<br>
     * @param inherit share the current event channel (including filters) to the new handler.
     * @param handler new handler
     */
    void registerListener(boolean inherit, IEventHandler<E> handler);

    IEventChannel<E> channel();
}
