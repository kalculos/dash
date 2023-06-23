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

package io.ib67.dash.adapter;

import io.ib67.dash.contact.IContact;
import io.ib67.dash.contact.IFriend;
import io.ib67.dash.contact.group.IChatGroup;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@ApiStatus.AvailableSince("0.1.0")
public abstract class PlatformAdapter {
    @Getter
    protected final String name;

    public List<? extends IFriend> getFriends() {
        return getAllContacts().stream().filter(it -> it instanceof IFriend).map(it -> (IFriend) it).toList();
    }

    public abstract Optional<? extends IContact> getContact(String platformId);

    public Optional<? extends IFriend> getFriend(String platformId) {
        return getContact(platformId).filter(it -> it instanceof IFriend).map(it -> (IFriend) it);
    }

    public Optional<? extends IChatGroup> getGroup(String platformId) {
        return getContact(platformId).filter(it -> it instanceof IChatGroup).map(it -> (IChatGroup) it);
    }

    public List<? extends IChatGroup> getChatGroups() {
        return getAllContacts().stream().filter(it -> it instanceof IChatGroup).map(it -> (IChatGroup) it).toList();
    }

    public abstract List<? extends IContact> getAllContacts();

    public abstract IContact getPlatformBot();
}
