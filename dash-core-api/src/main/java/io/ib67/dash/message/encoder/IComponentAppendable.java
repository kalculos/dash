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

package io.ib67.dash.message.encoder;

import io.ib67.dash.message.feature.IMessageComponent;
import io.ib67.dash.message.feature.component.Text;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.AvailableSince("0.1.0")
@FunctionalInterface
public interface IComponentAppendable {

    void add(IMessageComponent component) throws ComponentEncodeException;

    default IComponentAppendable append(IMessageComponent component) throws ComponentEncodeException{
        add(component);
        return this;
    }

    default IComponentAppendable append(String text) throws ComponentEncodeException{
        return append(new Text(text));
    }

    /**
     * For chained invocation like: <code>builder.register(Image.class, (a,b,c) -> b.append(a.toString()).ret(true))</code>
     *
     * @return
     */
    default boolean ret(boolean ret) {
        return ret;
    }
}
