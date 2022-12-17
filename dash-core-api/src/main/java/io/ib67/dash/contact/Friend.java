package io.ib67.dash.contact;

import io.ib67.dash.adapter.PlatformAdapter;
import io.ib67.dash.message.IMessageSource;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.AvailableSince("0.1.0")
public abstract class Friend extends Contact implements IMessageSource {
    protected Friend(long uid, String platformId, PlatformAdapter adapter) {
        super(uid, platformId, adapter);
    }

    @Override
    public String toString() {
        return "Friend(" + uid + "/" + idOnPlatform + " on " + platform.getName() + ")";
    }
}
