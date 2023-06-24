package io.ib67.dash.message.encoder;

import io.ib67.dash.message.AbstractMessage;
import io.ib67.dash.message.feature.IMessageComponent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.AvailableSince("0.1.0")
public interface IComponentEncoder<T extends IMessageComponent> {
    boolean encode(T component, IComponentAppendable appendable, @Nullable AbstractMessage message);
}
