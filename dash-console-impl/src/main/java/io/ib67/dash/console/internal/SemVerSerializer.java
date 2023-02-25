package io.ib67.dash.console.internal;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.zafarkhaja.semver.Version;

import java.io.IOException;

public class SemVerSerializer extends StdDeserializer<Version> {
    public SemVerSerializer(){
        this(null);
    }
    public SemVerSerializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Version deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        return Version.valueOf(p.getValueAsString());
    }
}
