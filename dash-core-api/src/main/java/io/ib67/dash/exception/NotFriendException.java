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

package io.ib67.dash.exception;

import io.ib67.dash.contact.group.Member;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.AvailableSince("0.1.0")
public class NotFriendException extends RuntimeException {
    private final Member member;

    public NotFriendException(Member member) {
        super();
        this.member = member;
    }

    public NotFriendException(String message, Member member) {
        super(message);
        this.member = member;
    }

    public NotFriendException(String message, Throwable cause, Member member) {
        super(message, cause);
        this.member = member;
    }

    public NotFriendException(Throwable cause, Member member) {
        super(cause);
        this.member = member;
    }

    protected NotFriendException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Member member) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.member = member;
    }
}
