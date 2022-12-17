package io.ib67.dash.message.group;

import io.ib67.dash.contact.group.channel.ChatChannel;
import io.ib67.dash.message.MessageChain;
import io.ib67.dash.message.feature.CompoundMessage;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.AvailableSince("0.1.0")
public class GroupChannelMessage extends CompoundMessage<ChatChannel> {
    public GroupChannelMessage(ChatChannel source, long id, MessageChain components) {
        super(source, id, components);
    }
}
