package io.ib67.dash.contact.group.channel;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import static java.util.Objects.requireNonNull;

public record ChannelInfo(
        String name,
        @Nullable String description,
        List<ChannelAnnouncement> announcements
) {
    public ChannelInfo {
        requireNonNull(name);
        requireNonNull(announcements);
    }
}
