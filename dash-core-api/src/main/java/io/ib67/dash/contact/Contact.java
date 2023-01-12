package io.ib67.dash.contact;

import io.ib67.dash.adapter.PlatformAdapter;
import io.ib67.dash.adapter.PlatformRelated;
import io.ib67.dash.tag.Taggable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

/**
 * Contact.<br>
 * A Contact is an object that may send messages to you on IM platform.
 */
@ApiStatus.AvailableSince("0.1.0")
@Getter
@EqualsAndHashCode
public abstract class Contact implements PlatformRelated, Taggable {
    /**
     * The user-id of the contact.
     * This ID is NOT platform ID. This is dash universal user id.
     */
    protected final long uid;

    /**
     * User ID from IM platform
     */
    protected final String idOnPlatform;

    protected final PlatformAdapter platform;

    /**
     * The name of the contact.
     */
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
}
