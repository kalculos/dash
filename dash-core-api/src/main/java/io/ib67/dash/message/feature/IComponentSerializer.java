package io.ib67.dash.message.feature;

import io.ib67.dash.serialization.ISerializer;
import io.ib67.dash.util.CatCodes;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link IComponentSerializer} is a Function that converts single {@link io.ib67.dash.util.CatCodes.CatCode} into a {@link IMessageComponent}.
 */
@ApiStatus.AvailableSince("0.1.0")
public interface IComponentSerializer extends ISerializer<CatCodes.CatCode, IMessageComponent> {
    @NotNull
    IMessageComponent deserialize(CatCodes.CatCode code) throws CatCodes.InvalidCatCodeException;

    @NotNull
    default CatCodes.CatCode serialize(IMessageComponent code) throws CatCodes.InvalidCatCodeException {
        return CatCodes.fromString(code.toCatCode()).get(0);
    }
}
