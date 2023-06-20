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

import io.ib67.dash.adapter.PlatformAdapter;
import io.ib67.dash.adapter.PlatformRelated;
import io.ib67.dash.context.ContextKey;
import io.ib67.dash.context.IContext;
import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.ContextualEvent;
import io.ib67.dash.message.internal.UnorderedEventContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * A {@link AbstractMessage} is an object that is composed of {@code context}, {@code sender} and {@code content}. <br />
 * The {@code context} enables you to mark or attach your data on this message (in its lifecycle). <br />
 * By using {@code context}, you can share data between interrupters and handlers.  <br />
 * <p>
 * Warning: The {@code context} is NOT thread-safe.
 */
@ApiStatus.AvailableSince("0.1.0")
@RequiredArgsConstructor
public abstract class AbstractMessage<S extends IMessageSource> extends AbstractEvent implements PlatformRelated, ContextualEvent {
    /**
     * The source of the message.
     */
    @Getter
    protected final S source;

    /**
     * The context of the message
     */
    @Getter
    private final IContext context = new UnorderedEventContext();

    /**
     * An universal message id.
     */
    @Getter
    protected final long id;

    /**
     * Some utility for writing reply codes quickly
     *
     * @param message response
     * @return if succeed
     */
    public boolean reply(MessageChain message) {
        return source.reply(id, message).isSent();
    }

    public boolean reply(String mesg) {
        return reply(MessageChain.fromLiteral(mesg));
    }

    @Override
    public PlatformAdapter getAdapter() {
        return source.getAdapter();
    }

    @Override
    public boolean hasContext(@Nullable ContextKey<?> key){
        return context.has(requireNonNull(key));
    }

    @Override
    public int hashCode() {
        int i = 1;
        i = i * 37 + source.hashCode();
        i = i * 31 + Long.hashCode(id);
        return i;
    }
}
