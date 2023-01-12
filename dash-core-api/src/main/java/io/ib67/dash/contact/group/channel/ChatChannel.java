package io.ib67.dash.contact.group.channel;

import io.ib67.dash.contact.group.ChatGroup;
import io.ib67.dash.message.IMessageSource;
import io.ib67.dash.tag.Taggable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

import static java.util.Objects.requireNonNull;

/**
 * A {@link ChatChannel} is a part of a {@link ChatGroup}, where you can receive/send messages.
 */
@Getter
@ApiStatus.AvailableSince("0.1.0")
@EqualsAndHashCode
public abstract class ChatChannel implements IMessageSource, Taggable {
    protected final ChannelInfo info;

    protected final ChatGroup group;

    public ChatChannel(ChannelInfo info, ChatGroup group) {
        requireNonNull(this.group = group);
        requireNonNull(this.info = info);
    }

    @Override
    public String toString() {
        return "ChatChannel(" + info.name() + " on " + group + ")";
    }
}
