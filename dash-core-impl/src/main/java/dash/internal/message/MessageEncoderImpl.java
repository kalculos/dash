package dash.internal.message;

import io.ib67.dash.message.AbstractMessage;
import io.ib67.dash.message.IMessageSource;
import io.ib67.dash.message.MessageChain;
import io.ib67.dash.message.encoder.ComponentEncodeException;
import io.ib67.dash.message.encoder.IComponentEncoder;
import io.ib67.dash.message.encoder.IMessageEncoder;
import io.ib67.dash.message.feature.CompoundMessage;
import io.ib67.dash.message.feature.IMessageComponent;
import io.ib67.dash.message.feature.component.Text;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
public class MessageEncoderImpl<S extends IMessageSource> implements IMessageEncoder<S> {
    private final Map<Class<?>, IComponentEncoder<?>> map;

    @Override
    public MessageChain encodeSingle(IMessageComponent component) throws ComponentEncodeException {
        var chain = new MessageChain();
        encodeSingle(component, chain, null);
        return chain;
    }

    @SuppressWarnings("unchecked")
    private void encodeSingle(IMessageComponent component, MessageChain chain, AbstractMessage<S> message) throws ComponentEncodeException {
        if (map.containsKey(component.getClass())) {
            var processor = (IComponentEncoder<IMessageComponent>) map.get(component.getClass());
            processor.encode(component, it -> encodeSingle(it, chain, message), message);
        } else {
            chain.add(component);
        }
    }

    @Override
    public MessageChain encodeByMessage(CompoundMessage<S> message) {
        var chain = new MessageChain(message.getComponents().size());
        for (IMessageComponent component : message) {
            encodeSingle(component,chain,message);
        }
        return chain;
    }

    @Override
    public <T extends IMessageSource> Builder<T> cloneEncoder() {
        return new BuilderImpl<>(new HashMap<>(map));
    }

    public static class BuilderImpl<T extends IMessageSource> implements IMessageEncoder.Builder<T> {
        private final Map<Class<?>, IComponentEncoder<?>> map;

        public BuilderImpl(Map<Class<?>, IComponentEncoder<?>> map) {
            Objects.requireNonNull(this.map = map);
        }

        @Override
        public IMessageEncoder.Builder<T> keep(Class<?>... classes) {
            for (Class<?> clazz : classes) {
                map.remove(clazz);
            }
            return this;
        }

        @Override
        public Builder<T> clear() {
            map.clear();
            return this;
        }

        @Override
        public <A extends IMessageComponent> IMessageEncoder.Builder<T> register(Class<A> clazz, IComponentEncoder<A> encoder) {
            if(clazz == Text.class){
                throw new UnsupportedOperationException("Text is the smallest unit to be sent.");
            }
            map.put(clazz, encoder);
            return this;
        }

        @Override
        public IMessageEncoder<T> build() {
            return new MessageEncoderImpl<>(map);
        }
    }
}
