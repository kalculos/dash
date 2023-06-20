/*
 * MIT License
 *
 * Copyright (c) 2023 Kalculos and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dash.internal.user;

import io.ib67.dash.user.IPermissionRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimplePermissionFactoryTest {
    private IPermissionRegistry permissionFactory;
    @BeforeEach
    public void setup(){
        permissionFactory = new SimplePermissionRegistry();
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
        assertEquals("a.*",permissionFactory.getNode("a").getNode());
    }

    private void testParseEqual(String s) {
        assertEquals(s,permissionFactory.getNode(s).getNode());
    }

    @Test
    public void testParseSpecial(){
        assertThrows(IllegalArgumentException.class,() ->permissionFactory.getNode("."));
        assertThrows(IllegalArgumentException.class,() ->permissionFactory.getNode(""));
        assertTrue(permissionFactory.getNode("*").matches(permissionFactory.getNode("any")));
        assertFalse(permissionFactory.getNode("-*").matches(permissionFactory.getNode("any")));
    }
}