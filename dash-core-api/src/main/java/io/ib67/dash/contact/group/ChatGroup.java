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
import io.ib67.dash.contact.group.channel.ChatChannel;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

/**
 * A {@link ChatGroup} is composed of {@link ChatChannel}s.<br>
 * Each ChatGroup has one default channel at least.
 */
@EqualsAndHashCode(callSuper = false)
public abstract class ChatGroup extends Contact {

    protected ChatGroup(long uid, String platformId, PlatformAdapter platform, List<ChatChannel> channels, ChatChannel defaultChannel, List<Member> members) {
        super(uid, platformId, platform);
        this.channels = channels;
        this.defaultChannel = defaultChannel;
        this.members = members;
    }

    @Getter
    protected final List<ChatChannel> channels;

    @Getter
    protected final ChatChannel defaultChannel;

    @Getter
    protected final List<Member> members;

    @Override
    public String toString() {
        return "Group(" + uid + "/" + platformUserId + " on " + platform.getName() + ")";
    }
}
