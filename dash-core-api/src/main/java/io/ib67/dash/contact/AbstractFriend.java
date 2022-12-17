package io.ib67.dash.contact;

import io.ib67.dash.message.IMessageSource;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.AvailableSince("0.1.0")
public abstract class AbstractFriend extends AbstractContact implements IMessageSource {
    protected AbstractFriend(long uid, String platformId) {
        super(uid, platformId);
    }
}
