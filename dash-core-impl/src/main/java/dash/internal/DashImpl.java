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
import dash.internal.serialization.RegularComponentSerializer;
import dash.internal.serialization.SimpleSerializerRegistry;
import dash.internal.user.SimplePermissionRegistry;
import io.ib67.dash.Dash;
import io.ib67.dash.adapter.IAdapterRegistry;
import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.IEventRegistry;
import io.ib67.dash.event.bus.IEventBus;
import io.ib67.dash.message.feature.IComponentSerializer;
import io.ib67.dash.serialization.ISerializerRegistry;
import io.ib67.dash.user.IPermissionRegistry;
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
    private final ExecutorService asyncPool;
    private final ScheduledExecutorService mainPool;
    private final IComponentSerializer componentSerializer;
    private final ISerializerRegistry serializerRegistry;
    private final IPermissionRegistry permissionFactory;
    private final IUserManager userManager;
    private final IEventBus bus;

    public DashImpl(Session session, ScheduledExecutorService main, ExecutorService async) {
        asyncPool = async;
        mainPool = main;
        bus = new DashEventBus(main, async);
        var channelFactory = bus.getChannelFactory();
        adapterRegistry = new SimpleAdapterRegistry();
        globalChannel = channelFactory.forMain("GLOBAL");
        eventRegistry = new SimpleEventRegistry(channelFactory);
        var _registry = new SimpleSerializerRegistry();
        componentSerializer = _registry;
        serializerRegistry = _registry;
        permissionFactory = new SimplePermissionRegistry();
        userManager = null;//new UserManagerImpl(session, permissionFactory, adapterRegistry);
        registerStandardCatCodes();
    }

    private void registerStandardCatCodes() {
        var regularSerializer = new RegularComponentSerializer();
        serializerRegistry.registerComponentSerializer("TEXT", regularSerializer);
        serializerRegistry.registerComponentSerializer("IMAGE", regularSerializer);
        serializerRegistry.registerComponentSerializer("AT", regularSerializer);
        serializerRegistry.registerComponentSerializer("FILE", regularSerializer);
        serializerRegistry.registerComponentSerializer("AUDIO", regularSerializer);
        serializerRegistry.registerComponentSerializer("STICKER", regularSerializer);
    }

    @Override
    public IComponentSerializer defaultComponentSerializer() {
        return componentSerializer;
    }
}
