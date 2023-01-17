package dash.internal.user.perm;

import io.ib67.dash.user.permission.Permission;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PermImplTest {
    @Test
    public void testEqual(){
        var permA = new PermImpl("a.b",false,"c",null);
        var permB = new PermImpl("a.b",false,"c","some description");
        assertEquals(permA, permB, "description can be different.");
        permA = new PermImpl("a.b",false,null,null);
        assertNotEquals(permA,permB,"equal is not matches");
        assertNotEquals(permA, new Object(), "perm is not equal to other type");

        @Getter
        class SimplePerm implements Permission {
            private final String node;
            private final String description = null;

            SimplePerm(String node) {
                this.node = node;
            }

            @Override
            public boolean matches(Permission permission) {
                return false;
            }
        }
        assertEquals(permA,new SimplePerm("a.b.*"),"equal to other permission when nodes are same");
    }

    @Test
    public void testMatches(){
        var permA = new PermImpl("a.b",false,null,null); // current:null means "*"
        var permB = new PermImpl("a.b",false,"c","some description");
        assertTrue(permA.matches(permB),"wildcard support, permB is in permA");
        assertFalse(permB.matches(permA),"permA is wider than permB.");
        permA = new PermImpl("a.b",false,"c",null);
        assertTrue(permA.matches(permB),"match by same properties except for description");
        assertTrue(permB.matches(permA),"permA == permB, permB == permA");
        permA = new PermImpl("a.b",false,null,null);
        permB = new PermImpl("a.b",false,null,null);
        assertTrue(permA.matches(permB),"wildcard matches wildcard");
        permA = new PermImpl("a.b",true,"c",null); // -a.b.c
        permB = new PermImpl("a.b",false,"c",null); // a.b.c
        assertFalse(permA.matches(permB),"result should be reversed");
        assertFalse(permB.matches(permA),"permB!=permA, permA!=permB");

        permA = new PermImpl("a.b",true,null,null); // -a.b.*
        permB = new PermImpl("a.b",false,"a",null);
        assertFalse(permA.matches(permB),"reversed wildcard.");
        permA = new PermImpl("a.b",false,null,null); // a.b.*
        permB = new PermImpl("a.b",true,"c",null); // -a.b.c
        assertFalse(permA.matches(permB),"a.b.* is not related to -a.b.c");

        // reversed?
        permA = new PermImpl("a.b",true,null,null);
        assertTrue(permA.matches(permB),"-a.b.c is in -a.b.*");
    }

    @Test
    public void testMatchesLayered(){
        var permA = new PermImpl("a.b.c",false,"d",null);
        var permB = new PermImpl("a.b",false,"c",null);
        assertFalse(permB.matches(permA),"a.b.c != a.b.c.d");
        assertFalse(permA.matches(permB),"a.b.c.d != a.b.c");
        permB = new PermImpl("a.b",false,null,null);
        assertTrue(permB.matches(permA),"a.b.c.d is in a.b.*");
        permB = new PermImpl("a.b",true,null,null);
        assertFalse(permB.matches(permA),"a.b.c.d is not in -a.b.*");
        permA = new PermImpl("a.b.c",true,"d",null);
        assertTrue(permB.matches(permA),"-a.b.c.d is in -a.b.*");;
    }

    @Test
    public void testToString() {
        var permA = new PermImpl("a.b", false, "c", null);
        assertEquals(permA.toString(), "a.b.c", "a.b.c");
        permA = new PermImpl("a.b", true, "c", null);
        assertEquals(permA.toString(), "-a.b.c", "-a.b.c");
        permA = new PermImpl("a.b", true, null, null);
        assertEquals(permA.toString(), "-a.b.*", "-a.b.*");
        permA = new PermImpl("a.b", false, null, null);
        assertEquals(permA.toString(), "a.b.*", "a.b.*");
        assertNull(permA.getCurrent());
        assertEquals(permA.getParent(),"a.b");
        assertNull(permA.getDescription());
        assertFalse(permA.isReversed());
    }
}