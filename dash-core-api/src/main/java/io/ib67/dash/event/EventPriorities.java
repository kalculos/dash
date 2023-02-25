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

package io.ib67.dash.event;

import org.jetbrains.annotations.ApiStatus;

/**
 * Here are some constants about preferred priorities for you.
 */
@ApiStatus.AvailableSince("0.1.0")
public final class EventPriorities {
    /**
     * This is for adapters. They receive events and then send it to IM servers, which needs to receive these events as fast as they can.
     */
    public static final int EARLIEST = 1;

    /**
     * Maybe some middlewares here.
     */
    public static final int EARLIER_THAN_NORMAL = 30;

    /**
     * Your business logics.
     */
    public static final int NORMAL = 50;

    /**
     * Some post-process logics.
     */
    public static final int SLOWER_THAN_NORMAL = 70;

    /**
     * For adapters. Actually you can be slower but not recommended
     */
    public static final int SLOWEST = 100;
}
