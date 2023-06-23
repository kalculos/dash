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

import io.ib67.dash.contact.IContact;
import io.ib67.dash.contact.IFriend;
import io.ib67.dash.context.IContext;
import io.ib67.dash.message.MessageChain;
import io.ib67.dash.user.IUser;
import io.ib67.dash.user.permission.IPermission;
import io.ib67.dash.user.permission.IPermissionContext;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A basic implementation of {@link IUser}.
 */
public class UserImpl implements IUser {
    protected final List<IPermission> permissions;
    @Getter
    protected final IContext context = IContext.create();
    @Getter
    protected final Collection<IContact> knownContacts;
    @Getter
    protected final IPermissionContext permissionContext;
    @Getter
    protected final long id;
    @Getter
    @Setter
    protected String name;

    public UserImpl(List<IPermission> permissions, Collection<IContact> knownContacts, IPermissionContext permissionContext, long id) {
        this.permissions = new ArrayList<>(permissions);
        this.knownContacts = knownContacts;
        this.permissionContext = permissionContext;
        this.id = id;
    }

    @Override
    public void broadcastMessage(@NotNull MessageChain message) {
        for (IContact knownContact : getKnownContacts()) {
            if (knownContact instanceof IFriend friend) {
                friend.sendMessage(message);
            }
        }
    }

    @Override
    public synchronized void grant(IPermission permission) {
        if (!hasPermission(permission)) {
            permissions.add(permission);
        }
    }

    @Override
    public synchronized void revoke(IPermission permission) {
        if (hasPermission(permission)) {
            permissions.remove(permission);
        }
    }

    @Override
    public Collection<? extends IPermission> getPermissions() {
        return new ArrayList<>(permissions);
    }

    @Override
    public boolean hasPermission(IPermissionContext context, IPermission permission) {
        return switch (context.lookup(this, permission)) {
            case BLOCK -> false;
            case BYPASS -> true;
            case DEFAULT -> permissions.contains(permission);
        };
    }
}
