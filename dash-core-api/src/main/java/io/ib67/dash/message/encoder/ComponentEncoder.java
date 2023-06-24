package io.ib67.dash.message.encoder;

import io.ib67.dash.message.AbstractMessage;
import io.ib67.dash.message.feature.IMessageComponent;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.AvailableSince("0.1.0")
public interface ComponentEncoder<T extends IMessageComponent> {
    boolean encode(T component, ComponentAppendable appendable, AbstractMessage message);
}
