package io.ib67.dash.serialization;

import io.ib67.dash.message.feature.IComponentSerializer;
import org.jetbrains.annotations.ApiStatus;

/**
 * A registry for {@link io.ib67.dash.message.feature.IComponentSerializer} <br>
 *
 * @implNote like {@code Map<String, List<IComponentSerializer>>}
 */
@ApiStatus.AvailableSince("0.1.0")
public interface ISerializerRegistry {
    void registerSerializer(String codeType, IComponentSerializer serializer);
    
    IComponentSerializer getSerializer(String codeType);
}
