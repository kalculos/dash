package dash.test.message;

import io.ib67.dash.message.ContextKey;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContextKeyTest {
    @Test
    public void test(){
        var initial = ContextKey.getCurrentIndex();
        var keyA = ContextKey.of("a");
        var anotherKeyA = ContextKey.of("A");
        assertSame(keyA,anotherKeyA,"ContextKeys are case ignored");
        assertEquals(initial+1,ContextKey.getCurrentIndex(),"Index should be 1");
        var keyB = ContextKey.of("b");
        assertNotEquals(keyB,keyA,"Keys with different name are not equal");
        assertEquals(initial+2,ContextKey.getCurrentIndex(),"Index should be 2");
    }
}