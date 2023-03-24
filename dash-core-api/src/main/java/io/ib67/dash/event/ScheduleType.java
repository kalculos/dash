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

import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

/**
 * Available choices for which thread your handler will be run on.
 */
@ApiStatus.AvailableSince("0.1.0")
public enum ScheduleType {
    /**
     * ASYNC means your handler will be called on another thread or a something like thread-pool.
     * It will be VirtualThreads when JEP 425 is out of incubator.
     * <p>
     * WARNING: ASYNC events will not guarantee your handler priority.
     * WARNING: Modifications in events will not be handled since order cannot be ensured
     *
     * @apiNote ASYNC is <strong>NOT FAST</strong>, You should only use it when you're having some blocking and time-series ignored codes.
     **/
    ASYNC(false),

    /**
     * MAIN means your handler will be called on the EventBus's MainThread and always earlier than ASYNC events.
     * @apiNote Write most of your business logics here.
     */
    MAIN(true),

    /**
     * MONITOR means your handler will be called on the thread which publishes the event.
     * For most situations, please use {@link #MAIN} or {@link #ASYNC} instead. This is only designed for some filters.
     * @apiNote It is for monitor plugin or adapters. Though it is fast, but we cannot guarantee which thread you're running on, which can lead to unsure behavior especially for blocking, time-series required codes.
     */
    MONITOR(true);

    @Getter
    private final boolean ordered;

    ScheduleType(boolean ordered) {
        this.ordered = ordered;
    }
}
