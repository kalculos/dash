package io.ib67.dash.contact.group;

import io.ib67.dash.contact.AbstractContact;
import io.ib67.dash.contact.AbstractFriend;
import io.ib67.dash.exception.NotFriendException;
import io.ib67.dash.message.IMessageSource;

public abstract class AbstractMember extends AbstractContact implements IMessageSource {
    protected AbstractMember(long uid, String platformId) {
        super(uid, platformId);
    }

    public abstract AbstractFriend asFriend() throws NotFriendException; // todo simple impl

    public abstract boolean isFriend();
}
