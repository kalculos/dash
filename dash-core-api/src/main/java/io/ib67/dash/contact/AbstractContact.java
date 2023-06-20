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

package io.ib67.dash.contact;

import io.ib67.dash.adapter.PlatformAdapter;
import io.ib67.dash.user.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * A Skeleton for adapter implementers.
 */
@Getter
@EqualsAndHashCode
public abstract class AbstractContact implements Contact{
    /**
     * User ID from IM platform
     */
    protected final String platformIdentifier;

    protected final PlatformAdapter adapter;

    /**
     * The name of the contact.
     */
    protected String name;

    protected AbstractContact(String platformIdentifier, PlatformAdapter platform) {
        this.platformIdentifier = platformIdentifier;
        this.adapter = platform;
    }

    public abstract User getUser();

    @Override
    public String toString() {
        return "Contact(" + getUser().getId() + "/" + getPlatformIdentifier() + " on " + getAdapter().getName() + ")";
    }
}
