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

package io.ib67.dash.contact.group.channel;

import io.ib67.dash.contact.group.ChatGroup;
import io.ib67.dash.message.IMessageSource;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.ApiStatus;

import static java.util.Objects.requireNonNull;

/**
 * A {@link ChatChannel} is a part of a {@link ChatGroup}, where you can receive/send messages.
 */
@ApiStatus.AvailableSince("0.1.0")
@EqualsAndHashCode
public abstract class ChatChannel implements IMessageSource {
    protected final ChannelInfo info;

    protected final ChatGroup group;

    public ChatChannel(ChannelInfo info, ChatGroup group) {
        requireNonNull(this.group = group);
        requireNonNull(this.info = info);
    }

    @Override
    public String toString() {
        return "ChatChannel(" + info.name() + " on " + group + ")";
    }
}
