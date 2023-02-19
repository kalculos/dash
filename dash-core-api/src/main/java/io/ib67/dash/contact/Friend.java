package io.ib67.dash.contact;

import io.ib67.dash.adapter.PlatformAdapter;
import io.ib67.dash.message.IMessageSource;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.AvailableSince("0.1.0")
@EqualsAndHashCode(callSuper = false)
public abstract class Friend extends Contact implements IMessageSource {
    protected Friend(long uid, String platformUserId, PlatformAdapter adapter) {
        super(uid, platformUserId, adapter);
    }

    @Override
    public String toString() {
        return "Friend(" + uid + "/" + platformUserId + " on " + platform.getName() + ")";
    }
}
