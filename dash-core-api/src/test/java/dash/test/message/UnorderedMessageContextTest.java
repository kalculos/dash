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

package dash.test.message;

import io.ib67.dash.context.ContextKey;
import io.ib67.dash.message.internal.UnorderedEventContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnorderedMessageContextTest {
    private static final ContextKey<Integer> KEY_A = ContextKey.of("c");
    private static final ContextKey<Integer> KEY_B = ContextKey.of("d");
    @Test
    public void testContextPut(){
        var context = new UnorderedEventContext();
        context.put(KEY_A,1);
        assertNull(context.get(KEY_B), "Unset key cannot get other keys' value");
        assertEquals(1,context.get(KEY_A),"Key is an index for a value");
    }

    @Test
    public void testRemove(){
        var context = new UnorderedEventContext();
        context.put(KEY_A,1);
        context.remove(KEY_A);
        assertFalse(context.has(KEY_A),"The relation is removed from the context");
    }
}
