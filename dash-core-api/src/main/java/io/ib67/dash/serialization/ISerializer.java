package io.ib67.dash.serialization;

import org.jetbrains.annotations.ApiStatus;

/**
 *
 * @param <P> primitive type
 * @param <C> complex type
 */
@ApiStatus.AvailableSince("0.1.0")
public interface ISerializer<P,C> {
    C deserialize(P primitive) throws SerializerException;
    P serialize(C complex) throws SerializerException;
}
