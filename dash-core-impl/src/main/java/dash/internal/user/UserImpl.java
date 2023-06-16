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

import io.ib67.dash.contact.Contact;
import io.ib67.dash.contact.Friend;
import io.ib67.dash.context.IContext;
import io.ib67.dash.message.MessageChain;
import io.ib67.dash.user.User;
import io.ib67.dash.user.permission.Permission;
import io.ib67.dash.user.permission.PermissionContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A basic implementation of {@link User}.
 */
@RequiredArgsConstructor
public class UserImpl implements User {
    protected final List<Permission> permissions;
    @Getter
    protected final IContext context = IContext.create();
    @Getter
    protected final Collection<Contact> knownContacts;
    @Getter
    protected final PermissionContext permissionContext;
    @Getter
    protected final long id;
    @Getter
    @Setter
    protected String name;

    @Override
    public void broadcastMessage(@NotNull MessageChain message) {
        for (Contact knownContact : getKnownContacts()) {
            if (knownContact instanceof Friend friend) {
                friend.sendMessage(message);
            }
        }
    }

    @Override
    public synchronized void grant(Permission permission) {
        if (!hasPermission(permission)) {
            permissions.add(permission);
        }
    }

    @Override
    public synchronized void revoke(Permission permission) {
        if (hasPermission(permission)) {
            permissions.remove(permission);
        }
    }

    @Override
    public Collection<? extends Permission> getPermissions() {
        return new ArrayList<>(permissions);
    }

    @Override
    public boolean hasPermission(PermissionContext context, Permission permission) {
        return switch (context.lookup(this, permission)) {
            case BLOCK -> false;
            case BYPASS -> true;
            case DEFAULT -> permissions.contains(permission);
        };
    }
}
