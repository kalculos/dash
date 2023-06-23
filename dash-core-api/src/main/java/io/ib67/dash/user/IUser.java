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

package io.ib67.dash.user;

import io.ib67.dash.contact.IContact;
import io.ib67.dash.context.IContext;
import io.ib67.dash.message.MessageChain;
import io.ib67.dash.user.permission.IAuthority;
import io.ib67.dash.user.permission.IPermission;
import io.ib67.dash.user.permission.IPermissionContext;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

@ApiStatus.AvailableSince("0.1.0")
public interface IUser extends IAuthority {
    /**
     * This is not persistent. Data will be lost after reinitialization etc.
     *
     * @return a memory-only context
     */
    @NotNull
    IContext getContext();

    /**
     * Fetch known contacts who were bind to this user.
     *
     * @return list of known contacts.
     */
    @NotNull
    Collection<? extends IContact> getKnownContacts();

    /**
     * Attempts to send message to every known contact.
     *
     * @param message message to be sent
     */
    void broadcastMessage(@NotNull MessageChain message);

    @NotNull
    IPermissionContext getPermissionContext();

    long getId();

    @Nullable
    String getName();

    void setName(String name);

    @Override
    default boolean hasPermission(IPermission perm) {
        return hasPermission(getPermissionContext(), perm);
    }
}
