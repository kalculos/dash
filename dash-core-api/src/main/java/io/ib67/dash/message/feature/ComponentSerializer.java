package io.ib67.dash.message.feature;

import io.ib67.dash.util.CatCodes;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link ComponentSerializer} is a Function that converts single {@link io.ib67.dash.util.CatCodes.CatCode} into a {@link MessageComponent}.
 */
@ApiStatus.AvailableSince("0.1.0")
@FunctionalInterface
public interface ComponentSerializer {
    @NotNull
    MessageComponent parse(CatCodes.CatCode code) throws CatCodes.InvalidCatCodeException;
}
