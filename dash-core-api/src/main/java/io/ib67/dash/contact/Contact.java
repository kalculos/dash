package io.ib67.dash.contact;

import io.ib67.dash.adapter.PlatformAdapter;
import io.ib67.dash.adapter.PlatformRelated;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

/**
 * Contact.<br>
 * A Contact is an object that may send messages to you on IM platform.
 */
@ApiStatus.AvailableSince("0.1.0")
public abstract class Contact implements PlatformRelated {
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
    protected final String idOnPlatform;
    @Getter
    protected final PlatformAdapter platform;
    /**
     * The name of the contact.
     */
    @Getter
    protected String name;

    protected Contact(long uid, String idOnPlatform, PlatformAdapter platform) {
        this.uid = uid;
        this.idOnPlatform = idOnPlatform;
        this.platform = platform;
    }

    @Override
    public String toString() {
        return "Contact(" + uid + "/" + getIdOnPlatform() + " on " + getPlatform().getName() + ")";
    }

    @Override
    public int hashCode() {
        var i = 1;
        i = i * 31 + Long.hashCode(uid);
        i = i * 31 + idOnPlatform.hashCode();
        return i;
    }
}
