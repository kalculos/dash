package dash.test.message;

import io.ib67.dash.message.ContextKey;
import io.ib67.dash.message.internal.ArrayMessageContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayMessageContextTest {
    private static final ContextKey<Integer> KEY_A = ContextKey.of("a");
    private static final ContextKey<Integer> KEY_B = ContextKey.of("b");
    @Test
    public void testContextPut(){
        var context = new ArrayMessageContext();
        context.put(KEY_A,1);
        assertNull(context.get(KEY_B), "Unset key cannot get other keys' value");
        assertEquals(1,context.get(KEY_A),"Key is an index for a value");
    }

    @Test
    public void testRemove(){
        var context = new ArrayMessageContext();
        context.put(KEY_A,1);
        context.remove(KEY_A);
        assertFalse(context.has(KEY_A),"The relation is removed from the context");
    }
}
