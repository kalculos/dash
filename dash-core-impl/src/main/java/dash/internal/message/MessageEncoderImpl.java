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
