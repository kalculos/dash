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

package io.ib67.dash.exception;

import io.ib67.dash.event.AbstractEvent;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

@Getter
@ApiStatus.AvailableSince("0.1.0")
public class EventException extends RuntimeException {
    private final AbstractEvent involvedEvent;

    public EventException(AbstractEvent involvedEvent) {
        super();
        this.involvedEvent = involvedEvent;
    }

    public EventException(String message, AbstractEvent involvedEvent) {
        super(message);
        this.involvedEvent = involvedEvent;
    }

    public EventException(String message, Throwable cause, AbstractEvent involvedEvent) {
        super(message, cause);
        this.involvedEvent = involvedEvent;
    }

    public EventException(Throwable cause, AbstractEvent involvedEvent) {
        super(cause);
        this.involvedEvent = involvedEvent;
    }

    protected EventException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, AbstractEvent involvedEvent) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.involvedEvent = involvedEvent;
    }
}
