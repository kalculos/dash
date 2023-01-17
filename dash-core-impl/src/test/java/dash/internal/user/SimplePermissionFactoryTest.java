package dash.internal.user;

import io.ib67.dash.user.IPermissionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class SimplePermissionFactoryTest {
    private IPermissionFactory permissionFactory;
    @BeforeEach
    public void setup(){
        permissionFactory = new SimplePermissionFactory();
    }
    @Test
    public void testPermCache(){
        var perm = permissionFactory.registerPermission("a.b.c",null);
        assertSame(permissionFactory.getNode("a.b.c"), perm);
    }

    @Test
    public void testParse(){
        testParseEqual("a.b.c");
        testParseEqual("-a.b.c");
        testParseEqual("-a.b.*");
        testParseEqual("a.b.*");
        assertEquals("a.*",permissionFactory.parseNode("a").getNode());
    }

    private void testParseEqual(String s) {
        assertEquals(s,permissionFactory.parseNode(s).getNode());
    }
}