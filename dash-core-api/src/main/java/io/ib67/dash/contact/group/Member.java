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

import io.ib67.dash.contact.Contact;
import io.ib67.dash.contact.Friend;
import io.ib67.dash.message.IMessageSource;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

/**
 * {@link Member}s are some {@link Contact} in the {@link ChatGroup}, while they are able to be {@link IMessageSource}s.
 */
@ApiStatus.AvailableSince("0.1.0")
public interface Member extends Contact, IMessageSource {
    default Optional<? extends Friend> asFriend() {
        return getAdapter().getFriend(getPlatformIdentifier());
    }

    ChatGroup getGroup();
}
