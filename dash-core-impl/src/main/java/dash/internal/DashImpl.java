package dash.internal;

import dash.internal.event.DashEventBus;
import dash.internal.registry.SimpleAdapterRegistry;
import dash.internal.registry.SimpleEventRegistry;
import dash.internal.serialization.RegularComponentSerializer;
import dash.internal.serialization.SimpleSerializerRegistry;
import dash.internal.user.SimplePermissionFactory;
import io.ib67.dash.Dash;
import io.ib67.dash.adapter.IAdapterRegistry;
import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.IEventRegistry;
import io.ib67.dash.event.bus.IEventBus;
import io.ib67.dash.message.feature.IComponentSerializer;
import io.ib67.dash.serialization.ISerializerRegistry;
import io.ib67.dash.user.IPermissionFactory;
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
    private final IPermissionFactory permissionFactory;
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
        permissionFactory = new SimplePermissionFactory();
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
