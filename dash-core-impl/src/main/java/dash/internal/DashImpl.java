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

package dash.internal;

import dash.internal.event.DashEventBus;
import dash.internal.registry.SimpleAdapterRegistry;
import dash.internal.registry.SimpleEventRegistry;
import dash.internal.user.SimplePermissionFactory;
import io.ib67.dash.Dash;
import io.ib67.dash.adapter.IAdapterRegistry;
import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.IEventRegistry;
import io.ib67.dash.event.bus.IEventBus;
import io.ib67.dash.user.IPermissionRegistry;
import io.ib67.dash.scheduler.Scheduler;
import io.ib67.dash.user.IUserManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.sf.persism.Session;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

@Getter
@AllArgsConstructor
public class DashImpl implements Dash {
    private final IAdapterRegistry adapterRegistry;
    private final IEventChannel<? extends AbstractEvent> globalChannel;
    private final IEventRegistry eventRegistry;
    private final IPermissionRegistry permissionRegistry;
    private final IUserManager userManager;
    private final IEventBus bus;
    private final Scheduler scheduler;

    public DashImpl(Session session, Scheduler scheduler) {
        this.scheduler = scheduler;
        bus = new DashEventBus(scheduler);
        var channelFactory = bus.getChannelFactory();
        adapterRegistry = new SimpleAdapterRegistry();
        globalChannel = channelFactory.forMain("GLOBAL");
        eventRegistry = new SimpleEventRegistry(channelFactory);
        permissionRegistry = new SimplePermissionFactory();
        userManager = null;//new UserManagerImpl(session, permissionFactory, adapterRegistry);
    }
}
