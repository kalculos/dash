package io.ib67.dash.contact;

import io.ib67.dash.message.IMessageSource;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.AvailableSince("0.1.0")
public abstract class Friend extends Contact implements IMessageSource {
    protected Friend(long uid, String platformId) {
        super(uid, platformId);
    }
}
