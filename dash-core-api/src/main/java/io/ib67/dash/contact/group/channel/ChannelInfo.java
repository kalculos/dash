package io.ib67.dash.contact.group.channel;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public record ChannelInfo(
        String name,
        @Nullable String description,
        List<ChannelAnnouncement> announcements
) {
    public ChannelInfo {
        requireNonNull(name);
        Objects.requireNonNull(announcements);
    }
}
