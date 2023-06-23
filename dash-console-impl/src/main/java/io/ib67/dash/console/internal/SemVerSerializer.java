package io.ib67.dash.console.internal;

import com.github.zafarkhaja.semver.Version;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class SemVerSerializer implements TypeSerializer<Version> {
    @Override
    public Version deserialize(Type type, ConfigurationNode node) throws SerializationException {
        if (node == null || node.virtual()) throw new SerializationException("Version cannot be null");
        return Version.valueOf(node.getString());
    }

    @Override
    public void serialize(Type type, @Nullable Version obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) throw new SerializationException("Version cannot be null");
        node.set(obj.toString());
    }
}
