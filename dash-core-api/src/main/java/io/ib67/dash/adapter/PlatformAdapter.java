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

import io.ib67.dash.contact.Contact;
import io.ib67.dash.contact.Friend;
import io.ib67.dash.contact.group.ChatGroup;
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

    public List<? extends Friend> getFriends() {
        return getAllContacts().stream().filter(it -> it instanceof Friend).map(it -> (Friend) it).toList();
    }

    public abstract Optional<? extends Contact> getContact(String platformId);

    public abstract Optional<? extends Contact> getContact(long id); // todo: integrate dash data-store to map UniversalUID -> PlatformUID

    public Optional<? extends Friend> getFriend(long id) {
        return getContact(id).filter(it -> it instanceof Friend).map(it -> (Friend) it);
    }

    public Optional<? extends Friend> getFriend(String platformId) {
        return getContact(platformId).filter(it -> it instanceof Friend).map(it -> (Friend) it);
    }

    public Optional<? extends ChatGroup> getGroup(long id) {
        return getContact(id).filter(it -> it instanceof ChatGroup).map(it -> (ChatGroup) it);
    }

    public Optional<? extends ChatGroup> getGroup(String platformId) {
        return getContact(platformId).filter(it -> it instanceof ChatGroup).map(it -> (ChatGroup) it);
    }

    public List<? extends ChatGroup> getChatGroups() {
        return getAllContacts().stream().filter(it -> it instanceof ChatGroup).map(it -> (ChatGroup) it).toList();
    }

    public abstract List<? extends Contact> getAllContacts();

    public abstract Contact getPlatformBot();
}
