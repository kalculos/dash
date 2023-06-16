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

package io.ib67.dash.contact.group;

import io.ib67.dash.adapter.PlatformAdapter;
import io.ib67.dash.contact.Contact;
import io.ib67.dash.contact.Friend;
import io.ib67.dash.exception.NotFriendException;
import io.ib67.dash.message.IMessageSource;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public abstract class Member extends Contact implements IMessageSource {
    protected final ChatGroup group;

    protected final boolean friend;

    protected Member(String idOnPlatform, PlatformAdapter adapter, ChatGroup group, boolean friend) {
        super(idOnPlatform, adapter);
        this.group = group;
        this.friend = friend;
    }

    public Friend asFriend() throws NotFriendException {
        return getPlatform().getFriend(getPlatformIdentifier()).orElseThrow(() -> new NotFriendException("This " + this + " from " + group + " isn't our friend.", this));
    }

    @Override
    public String toString() {
        return "Member(" + getUser().getId() + "/" + platformIdentifier + " on " + platform.getName() + ")";
    }
}
