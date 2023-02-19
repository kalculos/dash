package io.ib67.dash.tag;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.AvailableSince("0.1.0")
public interface Taggable {
    TagContainer getTagContainer();
}
