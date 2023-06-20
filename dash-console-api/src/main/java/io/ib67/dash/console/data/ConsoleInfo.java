package io.ib67.dash.console.data;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.AvailableSince("0.1.0")
public record ConsoleInfo(
        @NotNull String vendor,
        @NotNull String description,
        @NotNull String version
) {
}
