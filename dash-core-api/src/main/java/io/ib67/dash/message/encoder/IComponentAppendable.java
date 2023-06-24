package io.ib67.dash.message.encoder;

import io.ib67.dash.message.feature.IMessageComponent;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.AvailableSince("0.1.0")
public interface IComponentAppendable {
    void append(IMessageComponent component);
}
