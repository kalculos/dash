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
import dash.internal.user.SimplePermissionRegistry;
import io.ib67.dash.Dash;
import io.ib67.dash.adapter.IAdapterRegistry;
import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.IEventRegistry;
import io.ib67.dash.event.bus.IEventBus;
import io.ib67.dash.scheduler.IScheduler;
import io.ib67.dash.user.IPermissionRegistry;
import io.ib67.dash.user.IUserManager;
import lombok.*;

import static java.util.Objects.requireNonNull;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DashImpl implements Dash {
    private final IAdapterRegistry adapterRegistry;
    private final IEventChannel<? extends AbstractEvent> globalChannel;
    private final IEventRegistry eventRegistry;
    private final IPermissionRegistry permissionRegistry;
    private final IUserManager userManager;
    private final IEventBus bus;
    private final IScheduler scheduler;

    @Builder
    @Generated // to avoid unnecessary unit tests
    public static Dash create(IAdapterRegistry adapterRegistry, IEventBus bus, IEventRegistry eventRegistry,
                              IUserManager userManager, IScheduler scheduler, IPermissionRegistry permissionRegistry) {
        requireNonNull(adapterRegistry);
        requireNonNull(bus);
        requireNonNull(eventRegistry);
        requireNonNull(userManager);
        requireNonNull(scheduler);
        requireNonNull(permissionRegistry);
        return new DashImpl(
                adapterRegistry,
                bus.getChannelFactory().forMain("GLOBAL"),
                eventRegistry,
                permissionRegistry,
                userManager,
                bus,
                scheduler
        );
    }

    @Generated
    public static Dash createDefault(IScheduler scheduler, IUserManager userManager) {
        requireNonNull(scheduler);
        requireNonNull(userManager);
        var bus = new DashEventBus(scheduler);
        var channelFactory = bus.getChannelFactory();
        var adapterRegistry = new SimpleAdapterRegistry();
        var globalChannel = channelFactory.forMain("GLOBAL");
        var eventRegistry = new SimpleEventRegistry(channelFactory);
        var permissionRegistry = new SimplePermissionRegistry();
        return new DashImpl(adapterRegistry, globalChannel, eventRegistry, permissionRegistry, userManager, bus, scheduler);
    }
}
