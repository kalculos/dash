package io.ib67.dash.contact.group.channel;

import io.ib67.dash.contact.group.Member;

import static java.util.Objects.requireNonNull;

public record ChannelAnnouncement(
        Member sender,
        String message
) {
    public ChannelAnnouncement {
        requireNonNull(sender);
        requireNonNull(message);
    }
}
