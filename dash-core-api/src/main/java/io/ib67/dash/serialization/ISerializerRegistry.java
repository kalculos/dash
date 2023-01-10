package io.ib67.dash.serialization;

import io.ib67.dash.message.feature.IComponentSerializer;
import org.jetbrains.annotations.ApiStatus;

/**
 * A registry for {@link IComponentSerializer} <br>
 *
 * @implNote like {@code Map<String, List<IComponentSerializer>>}
 */
@ApiStatus.AvailableSince("0.1.0")
public interface ISerializerRegistry {
    void registerComponentSerializer(String codeType, IComponentSerializer serializer);

    IComponentSerializer getComponentSerializer(String codeType);

    <T> void registerSerializer(Class<T> type, ISerializer<?,T> serializer);

    <T> ISerializer<?,T> getSerializer(Class<T> type);
}
