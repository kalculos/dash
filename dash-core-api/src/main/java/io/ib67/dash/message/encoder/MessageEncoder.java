package io.ib67.dash.message.encoder;

import io.ib67.dash.message.IMessageSource;
import io.ib67.dash.message.MessageChain;
import io.ib67.dash.message.feature.CompoundMessage;
import io.ib67.dash.message.feature.IMessageComponent;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.AvailableSince("0.1.0")
public interface MessageEncoder<S extends IMessageSource> {
    MessageChain encodeSingle(IMessageComponent component);

    MessageChain encodeByMessage(CompoundMessage<S> message);

    <T extends IMessageSource> Builder<T> cloneEncoder();

    @ApiStatus.AvailableSince("0.1.0")
    interface Builder<S extends IMessageSource> {
        Builder<S> keep(Class<? extends IMessageComponent>... classes);

        <T extends IMessageComponent> Builder<S> register(Class<T> clazz, ComponentEncoder<T> encoder);

        MessageEncoder<S> build();
    }
}
