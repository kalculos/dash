package io.ib67.dash.adapter;

import org.jetbrains.annotations.ApiStatus;

/**
 * These objects are related to a specified {@link PlatformAdapter}
 */
@ApiStatus.AvailableSince("0.1.0")
public interface PlatformRelated {
    PlatformAdapter getPlatform();
}
