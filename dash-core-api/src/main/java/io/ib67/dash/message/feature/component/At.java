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

package io.ib67.dash.message.feature.component;

import io.ib67.dash.contact.Contact;
import io.ib67.dash.message.feature.IMessageComponent;
import io.ib67.dash.util.CatCodes;
import lombok.Builder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Mention a user.
 * Also see: {@link #unsafeAt(String, String)}
 * @param contact determines the universal user id.
 */
@ApiStatus.AvailableSince("0.1.0")
public record At(
        @Nullable Contact contact,
        @Nullable String platformId,
        @Nullable String display,
        @Nullable String platformUid
) implements IMessageComponent {
    /**
     * Create a new {@link At} directly
     *
     * @param platformId
     * @param platformUid
     * @return
     */
    public static At unsafeAt(String platformId, String platformUid) {
        return new At(null, platformId, platformUid, null);
    }

    @Builder
    public At {
    }

    @Override
    public String toCatCode() {
        if (contact != null) {
            return CatCodes.ofProps("target", String.valueOf(contact.getUid())).type("AT").toString();
        }
        return CatCodes.ofProps(
                "platform", platformId,
                "target", platformUid,
                "display",display
        ).type("AT").toString();
    }

    @Override
    public String toString() {
        return contact != null ? "@" + contact.getName() + " " :
                display != null ? "@"+display : "@"+platformId;
    }
}
