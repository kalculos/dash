package dash.internal.serialization;

import io.ib67.dash.message.feature.IComponentSerializer;
import io.ib67.dash.serialization.ISerializerRegistry;
import io.ib67.dash.util.CatCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleSerializerRegistryTest {
    private ISerializerRegistry registry;

    @BeforeEach
    public void setup() {
        registry = new SimpleSerializerRegistry();
    }

    @Test
    public void testFallback() {
        assertThrows(CatCodes.InvalidCatCodeException.class, () -> ((IComponentSerializer) registry).deserialize(null));
        assertThrows(CatCodes.InvalidCatCodeException.class, () -> ((IComponentSerializer) registry).deserialize(CatCodes.newCatCode("a")));
    }

    @Test
    public void testRegistration() {
        var regularSerializer = new RegularComponentSerializer();
        registry.registerComponentSerializer("TEXT", regularSerializer);
        assertSame(registry.getComponentSerializer("TEXT").get(), regularSerializer);
        var code = "[dash:TEXT,content=114514]";
        assertEquals(((IComponentSerializer)registry).deserialize(CatCodes.fromString(code).get(0)).toCatCode(),"114514");
    }
}