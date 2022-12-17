package io.ib67.dash.contact.group.channel;

import io.ib67.dash.contact.group.ChatGroup;
import io.ib67.dash.message.IMessageSource;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

import static java.util.Objects.requireNonNull;

/**
 * A {@link ChatChannel} is a part of a {@link ChatGroup}, where you can receive/send messages.
 */
@ApiStatus.AvailableSince("0.1.0")
public abstract class ChatChannel implements IMessageSource {
    @Getter
    protected ChannelInfo info;

    public ChatChannel(ChannelInfo info) {
        requireNonNull(this.info = info);
    }
}
