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

package io.ib67.dash;

import io.ib67.dash.adapter.IAdapterRegistry;
import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.bus.IEventBus;
import io.ib67.dash.internal.DashInstFiner;
import io.ib67.dash.user.IPermissionRegistry;
import io.ib67.dash.scheduler.Scheduler;
import io.ib67.dash.user.IUserManager;
import org.jetbrains.annotations.ApiStatus;

/**
 * The core part of dash framework.<br>
 * You can find everything you need in this class. They are shared among multiple {@link AbstractBot}s.
 */
@ApiStatus.AvailableSince("0.1.0")
public interface Dash {
    static Dash getInstance() {
        return DashInstFiner.FINDER.get();
    }

    IAdapterRegistry getAdapterRegistry();

    /**
     * Where you can receive all events.
     *
     * @return global event channel.
     */
    IEventChannel<? extends AbstractEvent> getGlobalChannel();

    IEventBus getBus();

    Scheduler getScheduler();

    IPermissionRegistry getPermissionRegistry();

    IUserManager getUserManager();
}
