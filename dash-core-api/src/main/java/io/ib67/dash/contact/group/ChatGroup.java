package io.ib67.dash.contact.group;

import io.ib67.dash.adapter.PlatformAdapter;
import io.ib67.dash.contact.Contact;
import io.ib67.dash.contact.group.channel.ChatChannel;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

/**
 * A {@link ChatGroup} is composed of {@link ChatChannel}s.<br>
 * Each ChatGroup has one default channel at least.
 */
@EqualsAndHashCode(callSuper = false)
public abstract class ChatGroup extends Contact {

    protected ChatGroup(long uid, String platformId, PlatformAdapter platform, List<ChatChannel> channels, ChatChannel defaultChannel, List<Member> members) {
        super(uid, platformId, platform);
        this.channels = channels;
        this.defaultChannel = defaultChannel;
        this.members = members;
    }

    @Getter
    protected final List<ChatChannel> channels;

    @Getter
    protected final ChatChannel defaultChannel;

    @Getter
    protected final List<Member> members;

    @Override
    public String toString() {
        return "Group(" + uid + "/" + idOnPlatform + " on " + platform.getName() + ")";
    }
}
