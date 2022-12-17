package io.ib67.dash.contact.group;

import io.ib67.dash.contact.AbstractContact;
import io.ib67.dash.contact.group.channel.AbstractChatChannel;

import java.util.List;

/**
 * A {@link AbstractChatGroup} is composed of {@link AbstractChatChannel}s.<br>
 * Each ChatGroup has one default channel at least.
 */
public abstract class AbstractChatGroup extends AbstractContact {

    protected AbstractChatGroup(long uid, String platformId) {
        super(uid, platformId);
    }

    public abstract AbstractChatChannel getDefaultChannel();

    public abstract List<AbstractChatChannel> getChannels();

    public abstract List<AbstractMember> getMembers();
}
