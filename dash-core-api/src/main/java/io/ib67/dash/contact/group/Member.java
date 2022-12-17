package io.ib67.dash.contact.group;

import io.ib67.dash.adapter.PlatformAdapter;
import io.ib67.dash.contact.Contact;
import io.ib67.dash.contact.Friend;
import io.ib67.dash.exception.NotFriendException;
import io.ib67.dash.message.IMessageSource;
import lombok.Getter;

public abstract class Member extends Contact implements IMessageSource {
    @Getter
    protected final ChatGroup group;

    protected Member(long uid, String idOnPlatform, PlatformAdapter adapter, ChatGroup group) {
        super(uid, idOnPlatform, adapter);
        this.group = group;
    }

    public Friend asFriend() throws NotFriendException {
        return getPlatform().getFriend(uid).orElseThrow(() -> new NotFriendException("This " + toString() + " from " + group + " isn't our friend.", this));

    }

    public abstract boolean isFriend();

    @Override
    public String toString() {
        return "Member(" + uid + "/" + idOnPlatform + " on " + platform.getName() + ")";
    }
}
