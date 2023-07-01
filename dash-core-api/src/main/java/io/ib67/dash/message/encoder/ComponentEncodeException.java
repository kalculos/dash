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

package io.ib67.dash.message.encoder;

import io.ib67.dash.message.feature.IMessageComponent;
import lombok.Generated;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

/**
 * Thrown if there are some components that cannot be encoded.
 */
@ApiStatus.AvailableSince("0.1.0")
public class ComponentEncodeException extends RuntimeException{
    /**
     * The involved component.
     */
    @Getter
    private final IMessageComponent component;

    @Generated
    public ComponentEncodeException(IMessageComponent component) {
        super();
        this.component = component;
    }

    @Generated
    public ComponentEncodeException(String message, IMessageComponent component) {
        super(message);
        this.component = component;
    }

    @Generated
    public ComponentEncodeException(String message, Throwable cause, IMessageComponent component) {
        super(message, cause);
        this.component = component;
    }

    @Generated
    public ComponentEncodeException(Throwable cause, IMessageComponent component) {
        super(cause);
        this.component = component;
    }

    @Generated
    protected ComponentEncodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, IMessageComponent component) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.component = component;
    }
}
