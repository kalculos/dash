package io.ib67.dash.message.encoder;

import io.ib67.dash.message.IMessageSource;
import io.ib67.dash.message.MessageChain;
import io.ib67.dash.message.feature.CompoundMessage;
import io.ib67.dash.message.feature.IMessageComponent;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.AvailableSince("0.1.0")
public interface IMessageEncoder<S extends IMessageSource> {
    MessageChain encodeSingle(IMessageComponent component) throws ComponentEncodeException;

    MessageChain encodeByMessage(CompoundMessage<S> message) throws ComponentEncodeException;

    <T extends IMessageSource> Builder<T> cloneEncoder();

    @ApiStatus.AvailableSince("0.1.0")
    interface Builder<S extends IMessageSource> {
        Builder<S> keep(Class<?>... classes);

        /**
         * Equals to keepAll
         * @return this
         */
        Builder<S> clear();

        /**
         * @throws UnsupportedOperationException if clazz == Text.class
         */
        <T extends IMessageComponent> Builder<S> register(Class<T> clazz, IComponentEncoder<T> encoder);

        IMessageEncoder<S> build();
    }
}
