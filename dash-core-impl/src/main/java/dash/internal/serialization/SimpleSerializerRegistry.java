package dash.internal.serialization;

import io.ib67.dash.message.feature.IComponentSerializer;
import io.ib67.dash.message.feature.IMessageComponent;
import io.ib67.dash.serialization.ISerializer;
import io.ib67.dash.serialization.ISerializerRegistry;
import io.ib67.dash.util.CatCodes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class SimpleSerializerRegistry implements ISerializerRegistry, IComponentSerializer {
    private final Map<String, IComponentSerializer> serializers = new HashMap<>();
    private final Map<Class<?>, ISerializer<?,?>> dataSerializers = new HashMap<>();

    @Override
    public void registerComponentSerializer(String codeType, IComponentSerializer serializer) {
        serializers.put(codeType.toUpperCase(), serializer);
    }

    @Override
    public IComponentSerializer getComponentSerializer(String codeType) {
        return serializers.get(codeType.toUpperCase());
    }

    @Override
    public <T> void registerSerializer(Class<T> type, ISerializer<?, T> serializer) {
        dataSerializers.put(type,serializer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ISerializer<?, T> getSerializer(Class<T> type) {
        return (ISerializer<?, T>) dataSerializers.get(type);
    }

    @Override
    public @NotNull IMessageComponent deserialize(CatCodes.CatCode code) throws CatCodes.InvalidCatCodeException {
        return serializers.getOrDefault(code.getType(), FailureSerializer.INSTANCE).deserialize(code);
    }

    @Slf4j
    private static class FailureSerializer implements IComponentSerializer {
        private static final FailureSerializer INSTANCE = new FailureSerializer();

        @Override
        public @NotNull IMessageComponent deserialize(CatCodes.CatCode code) throws CatCodes.InvalidCatCodeException {
            throw new CatCodes.InvalidCatCodeException("Cannot find serializer for " + code.getType());
        }
    }
}
