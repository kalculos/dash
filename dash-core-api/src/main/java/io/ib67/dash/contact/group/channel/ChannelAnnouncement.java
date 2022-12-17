package io.ib67.dash.contact.group.channel;

import io.ib67.dash.contact.group.AbstractMember;

import static java.util.Objects.requireNonNull;

public record ChannelAnnouncement(
        AbstractMember sender,
        String message
) {
    public ChannelAnnouncement {
        requireNonNull(sender);
        requireNonNull(message);
    }
}
