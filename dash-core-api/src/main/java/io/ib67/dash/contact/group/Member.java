package io.ib67.dash.contact.group;

import io.ib67.dash.contact.Contact;
import io.ib67.dash.contact.Friend;
import io.ib67.dash.exception.NotFriendException;
import io.ib67.dash.message.IMessageSource;

public abstract class Member extends Contact implements IMessageSource {
    protected Member(long uid, String platformId) {
        super(uid, platformId);
    }

    public abstract Friend asFriend() throws NotFriendException; // todo simple impl

    public abstract boolean isFriend();
}
