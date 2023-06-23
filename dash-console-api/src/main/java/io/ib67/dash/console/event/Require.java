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

package io.ib67.dash.console.event;

import io.ib67.dash.event.IEventListener;
import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.*;

/**
 * Some filters for handler methods.
 * See also: {@link IEventListener}
 */
@ApiStatus.AvailableSince("0.1.0")
public class Require {
    private Require() {
        throw new IllegalArgumentException(":(");
    }

    /**
     * Indicating some permission authentication are required before calling the annotated {@link io.ib67.dash.event.EventHandler} _method_.
     */
    @ApiStatus.AvailableSince("0.1.0")
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Permission {
        String value();

        boolean revert() default false;
    }

    /**
     * Indicating the existence of some {@link io.ib67.dash.context.ContextKey} is required before calling the annotated {@link io.ib67.dash.event.EventHandler} _method_.
     */
    @ApiStatus.AvailableSince("0.1.0")
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Context {
        String value();

        boolean revert() default false;
    }

    /**
     * Indicating the message must come from a specified adapter.
     */
    @ApiStatus.AvailableSince("0.1.0")
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Platform {
        String value();

        boolean revert() default false;
    }
}
