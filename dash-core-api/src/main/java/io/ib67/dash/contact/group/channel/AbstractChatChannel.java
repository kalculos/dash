package io.ib67.dash.contact.group.channel;

import io.ib67.dash.contact.group.AbstractChatGroup;
import io.ib67.dash.message.IMessageSource;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

import static java.util.Objects.requireNonNull;

/**
 * A {@link AbstractChatChannel} is a part of a {@link AbstractChatGroup}, where you can receive/send messages.
 */
@ApiStatus.AvailableSince("0.1.0")
public abstract class AbstractChatChannel implements IMessageSource {
    @Getter
    protected ChannelInfo info;

    public AbstractChatChannel(ChannelInfo info) {
        requireNonNull(this.info = info);
    }
}
