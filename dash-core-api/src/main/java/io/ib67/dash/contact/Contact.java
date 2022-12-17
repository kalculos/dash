package io.ib67.dash.contact;

import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

/**
 * Contact.<br>
 * A Contact is an object that may send messages to you on IM platform.
 */
@ApiStatus.AvailableSince("0.1.0")
public abstract class Contact {
    /**
     * The user-id of the contact.
     * This ID is NOT platform ID. This is dash universal user id.
     */
    @Getter
    protected final long uid;
    /**
     * User ID from IM platform
     */
    @Getter
    protected final String platformId;
    /**
     * The name of the contact.
     */
    @Getter
    protected String name;

    protected Contact(long uid, String platformId) {
        this.uid = uid;
        this.platformId = platformId;
    }

    @Override
    public int hashCode() {
        int i = 1;
        i = i * 31 + Long.hashCode(uid);
        i = i * 31 + platformId.hashCode();
        return i;
    }
}
