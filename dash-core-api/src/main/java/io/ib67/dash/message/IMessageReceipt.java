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

package io.ib67.dash.message;

import org.jetbrains.annotations.ApiStatus;

/**
 * A {@link IMessageReceipt} allows you to control sent messages more fluently.
 */
@ApiStatus.AvailableSince("0.1.0")
public interface IMessageReceipt {
    /**
     * Recall your message, can be failed.
     *
     * @return if succeed
     */
    boolean tryRecall();

    /**
     * Edit your message, can be failed
     *
     * @param newContent new content
     * @return if succeed
     */
    boolean tryEdit(MessageChain newContent);

    /**
     * To check if your bot is muted, kicked or the contact isn't exists.
     *
     * @return if the message is sent successfully.
     */
    boolean isSent();
}
