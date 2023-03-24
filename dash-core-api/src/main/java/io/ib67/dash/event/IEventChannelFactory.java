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
import org.jetbrains.annotations.Nullable;

import static io.ib67.dash.event.IEventChannel.DEFAULT_PRIORITY;

/**
 * An {@link IEventChannelFactory} creates new event channel with configurations.
 */
@ApiStatus.AvailableSince("0.1.0")
public interface IEventChannelFactory {
    /**
     * Create a new {@link IEventChannel} from given properties.
     *
     * @param scheduleType determines which thread your codes runs on, please refer to {@link ScheduleType} for details, especially ASYNC codes.
     * @param name         the name of your channel, nullable, only for debugging purpose.
     * @param priority     the priority of your handler, lower is earlier. Must be positive
     * @param <E>          type of event.
     * @return a new event channel.
     */
    <E extends AbstractEvent> IEventChannel<E> from(ScheduleType scheduleType, @Nullable String name, int priority);

    /* OVERLOADS */

    /**
     * This is an overload, Please refer to {@link #from(ScheduleType, String, int)}
     */
    default <E extends AbstractEvent> IEventChannel<E> of(String name) {
        return from(ScheduleType.MAIN, name, DEFAULT_PRIORITY);
    }

    /**
     * This is an overload, Please refer to {@link #from(ScheduleType, String, int)}
     */
    default <E extends AbstractEvent> IEventChannel<E> of(String name, int priority) {
        return from(ScheduleType.MAIN, name, priority);
    }

    /**
     * This is an overload, Please refer to {@link #from(ScheduleType, String, int)}
     */
    default <E extends AbstractEvent> IEventChannel<E> of(int priority) {
        return from(ScheduleType.MAIN, null, priority);
    }

    /**
     * This is an overload, Please refer to {@link #from(ScheduleType, String, int)}
     */
    default <E extends AbstractEvent> IEventChannel<E> of(ScheduleType scheduleType) {
        return from(scheduleType, null, DEFAULT_PRIORITY);
    }

    /**
     * This is an overload, Please refer to {@link #from(ScheduleType, String, int)}
     */
    default <E extends AbstractEvent> IEventChannel<E> of(ScheduleType scheduleType, int priority) {
        return from(scheduleType, null, priority);
    }

    /**
     * This is an overload, Please refer to {@link #from(ScheduleType, String, int)}
     */
    default <E extends AbstractEvent> IEventChannel<E> forAsync() {
        return from(ScheduleType.ASYNC, null, DEFAULT_PRIORITY);
    }

    /**
     * This is an overload, Please refer to {@link #from(ScheduleType, String, int)}
     */
    default <E extends AbstractEvent> IEventChannel<E> forAsync(String name, int priority) {
        return from(ScheduleType.ASYNC, name, priority);
    }

    /**
     * This is an overload, Please refer to {@link #from(ScheduleType, String, int)}
     */
    default <E extends AbstractEvent> IEventChannel<E> forAsync(String name) {
        return from(ScheduleType.ASYNC, name, DEFAULT_PRIORITY);
    }

    /**
     * This is an overload, Please refer to {@link #from(ScheduleType, String, int)}
     */
    default <E extends AbstractEvent> IEventChannel<E> forAsync(int priority) {
        return from(ScheduleType.ASYNC, null, priority);
    }

    /**
     * This is an overload, Please refer to {@link #from(ScheduleType, String, int)}
     */
    default <E extends AbstractEvent> IEventChannel<E> forMain() {
        return from(ScheduleType.MAIN, null, DEFAULT_PRIORITY);
    }

    /**
     * This is an overload, Please refer to {@link #from(ScheduleType, String, int)}
     */
    default <E extends AbstractEvent> IEventChannel<E> forMain(String name, int priority) {
        return from(ScheduleType.MAIN, name, priority);
    }

    /**
     * This is an overload, Please refer to {@link #from(ScheduleType, String, int)}
     */
    default <E extends AbstractEvent> IEventChannel<E> forMain(String name) {
        return from(ScheduleType.MAIN, name, DEFAULT_PRIORITY);
    }

    /**
     * This is an overload, Please refer to {@link #from(ScheduleType, String, int)}
     */
    default <E extends AbstractEvent> IEventChannel<E> forMain(int priority) {
        return from(ScheduleType.MAIN, null, priority);
    }

    /**
     * This is an overload, Please refer to {@link #from(ScheduleType, String, int)}
     */
    default <E extends AbstractEvent> IEventChannel<E> forMonitor() {
        return from(ScheduleType.MONITOR, null, DEFAULT_PRIORITY);
    }

    /**
     * This is an overload, Please refer to {@link #from(ScheduleType, String, int)}
     */
    default <E extends AbstractEvent> IEventChannel<E> forMonitor(String name, int priority) {
        return from(ScheduleType.MONITOR, name, priority);
    }

    /**
     * This is an overload, Please refer to {@link #from(ScheduleType, String, int)}
     */
    default <E extends AbstractEvent> IEventChannel<E> forMonitor(String name) {
        return from(ScheduleType.MONITOR, name, DEFAULT_PRIORITY);
    }

    /**
     * This is an overload, Please refer to {@link #from(ScheduleType, String, int)}
     */
    default <E extends AbstractEvent> IEventChannel<E> forMonitor(int priority) {
        return from(ScheduleType.MONITOR, null, priority);
    }
}
