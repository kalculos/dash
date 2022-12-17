package io.ib67.dash.contact.group;

import io.ib67.dash.adapter.PlatformAdapter;
import io.ib67.dash.contact.Contact;
import io.ib67.dash.contact.group.channel.ChatChannel;

import java.util.List;

/**
 * A {@link ChatGroup} is composed of {@link ChatChannel}s.<br>
 * Each ChatGroup has one default channel at least.
 */
public abstract class ChatGroup extends Contact {

    protected ChatGroup(long uid, String platformId, PlatformAdapter platform) {
        super(uid, platformId, platform);
    }

    public abstract ChatChannel getDefaultChannel();

    public abstract List<ChatChannel> getChannels();

    public abstract List<Member> getMembers();

    @Override
    public String toString() {
        return "Group(" + uid + "/" + idOnPlatform + " on " + platform.getName() + ")";
    }
}
