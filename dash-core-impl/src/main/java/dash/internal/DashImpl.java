package dash.internal;

import dash.internal.event.DashEventBus;
import dash.internal.event.SimpleEventChannelFactory;
import dash.internal.registry.SimpleAdapterRegistry;
import dash.internal.registry.SimpleEventRegistry;
import dash.internal.serialization.RegularComponentSerializer;
import dash.internal.serialization.SimpleSerializerRegistry;
import io.ib67.dash.Dash;
import io.ib67.dash.adapter.IAdapterRegistry;
import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.IEventChannel;
import io.ib67.dash.event.IEventChannelFactory;
import io.ib67.dash.event.IEventRegistry;
import io.ib67.dash.message.feature.IComponentSerializer;
import io.ib67.dash.serialization.ISerializerRegistry;
import io.ib67.dash.storage.IContainerFactory;
import io.ib67.kiwi.Kiwi;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

@Getter
@AllArgsConstructor
public class DashImpl implements Dash {
    private final IAdapterRegistry adapterRegistry;
    private final IEventChannelFactory channelFactory;
    private final IEventChannel<? extends AbstractEvent> globalChannel;
    private final IEventRegistry eventRegistry;
    private final ScheduledExecutorService pool;
    private final IComponentSerializer componentSerializer;
    private final ISerializerRegistry serializerRegistry;

    public DashImpl(ExecutorService main, ScheduledExecutorService async) {
        pool = async;
        channelFactory = new SimpleEventChannelFactory(new DashEventBus(main, async));
        adapterRegistry = new SimpleAdapterRegistry();
        globalChannel = channelFactory.forMain("GLOBAL");
        eventRegistry = new SimpleEventRegistry(channelFactory);
        var _registry = new SimpleSerializerRegistry();
        componentSerializer = _registry;
        serializerRegistry = _registry;
        registerStandardCatCodes();
    }

    private void registerStandardCatCodes() {
        var regularSerializer = new RegularComponentSerializer();
        serializerRegistry.registerComponentSerializer("TEXT",regularSerializer);
        serializerRegistry.registerComponentSerializer("IMAGE",regularSerializer);
        serializerRegistry.registerComponentSerializer("AT",regularSerializer);
        serializerRegistry.registerComponentSerializer("FILE",regularSerializer);
        serializerRegistry.registerComponentSerializer("AUDIO",regularSerializer);
        serializerRegistry.registerComponentSerializer("STICKER",regularSerializer);
    }

    @Override
    public IComponentSerializer defaultComponentSerializer() {
        return componentSerializer;
    }

    @Override
    public IContainerFactory getContainerFactory() {
        return Kiwi.todo("container factory");
    }
}
