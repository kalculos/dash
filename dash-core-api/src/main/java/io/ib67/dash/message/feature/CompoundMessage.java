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

package io.ib67.dash.message.feature;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.ib67.dash.message.AbstractMessage;
import io.ib67.dash.message.IMessageSource;
import io.ib67.dash.message.MessageChain;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

/**
 * A {@link CompoundMessage} is a set of {@link IMessageComponent}, which is mutable.
 *
 * @param <S>
 */
@ApiStatus.AvailableSince("0.1.0")
@Getter
public class CompoundMessage<S extends IMessageSource> extends AbstractMessage<S> {
    /**
     * The content of {@link CompoundMessage}, aka "MessageChain"
     */
    private final MessageChain components;

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public CompoundMessage(S source, long id, MessageChain components) {
        super(source, id);
        Objects.requireNonNull(this.components = components);
    }

    public boolean containString(String str) {
        return components.containString(str);
    }

    @Contract("_, _ -> new")
    public CompoundMessage<S> replaceAll(String regex, String toBe) {
        var newChain = components.replaceAll(regex, toBe);
        return new CompoundMessage<>(source, id, newChain);
    }

}
