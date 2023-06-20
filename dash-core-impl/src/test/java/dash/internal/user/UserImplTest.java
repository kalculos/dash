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

import dash.test.contact.MockContact;
import io.ib67.dash.adapter.PlatformAdapter;
import io.ib67.dash.contact.Friend;
import io.ib67.dash.message.IMessageReceipt;
import io.ib67.dash.message.MessageChain;
import io.ib67.dash.user.User;
import io.ib67.dash.user.permission.PermissionContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class UserImplTest {
    private User testSubject;

    @BeforeEach
    public void setup() {
        testSubject = new UserImpl(
                new ArrayList<>(),
                List.of(),
                PermissionContext.DEFAULT,
                0
        );
    }

    @Test
    public void testGrant() {
        var pf = new SimplePermissionFactory();
        var perm = pf.getNode("a.b");
        assertFalse(testSubject.hasPermission(perm));
        testSubject.grant(perm);
        assertTrue(testSubject.hasPermission(perm));
        testSubject.grant(perm);
        assertEquals(1, testSubject.getPermissions().size());
        assertNotSame(testSubject.getPermissions(), testSubject.getPermissions());
    }

    @Test
    public void testRevoke() {
        var pf = new SimplePermissionFactory();
        var perm = pf.getNode("a.b");
        testSubject = new UserImpl(
                new ArrayList<>(List.of(perm)),
                List.of(),
                PermissionContext.DEFAULT,
                0
        );
        assertTrue(testSubject.hasPermission(perm));
        testSubject.revoke(perm);
        testSubject.revoke(perm);
        assertFalse(testSubject.hasPermission(perm));
    }

    @Test
    public void testBroadcastMessage() {
        var counter = new AtomicInteger();
        class MockFriend extends MockContact implements Friend {
            public MockFriend(String platformIdentifier, PlatformAdapter platform, User user) {
                super(platformIdentifier, platform, user);
            }

            @Override
            public IMessageReceipt sendMessage(MessageChain message) {
                counter.incrementAndGet();
                return null;
            }

            @Override
            public IMessageReceipt reply(long messageId, MessageChain chain) {
                return null;
            }

            @Override
            public IMessageReceipt recall(long messageId) {
                return null;
            }
        }
        testSubject = new UserImpl(
                List.of(),
                List.of(new MockFriend(null, null, null)),
                PermissionContext.DEFAULT,
                0
        );
        testSubject.broadcastMessage(null);
        assertEquals(counter.get(), 1);
    }

    @Test
    public void testHasPermission(){
        var pf = new SimplePermissionFactory();
        var perm = pf.getNode("a.b");
        PermissionContext context = PermissionContext.DEFAULT;
        testSubject = new UserImpl(
                List.of(perm),
                List.of(),
                context,0
        );
        assertTrue(testSubject.hasPermission(perm));
        context = (a,b) -> PermissionContext.Result.BLOCK;
        testSubject = new UserImpl(
                List.of(perm),
                List.of(),
                context,0
        );
        assertFalse(testSubject.hasPermission(perm));
        context = (a,b) -> PermissionContext.Result.BYPASS;
        testSubject = new UserImpl(
                List.of(perm),
                List.of(),
                context,0
        );
        assertTrue(testSubject.hasPermission(pf.getNode("nothing")));
    }
}
