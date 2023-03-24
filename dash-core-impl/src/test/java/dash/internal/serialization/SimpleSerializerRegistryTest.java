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

package dash.internal.serialization;

import io.ib67.dash.message.feature.IComponentSerializer;
import io.ib67.dash.serialization.ISerializerRegistry;
import io.ib67.dash.util.CatCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleSerializerRegistryTest {
    private ISerializerRegistry registry;

    @BeforeEach
    public void setup() {
        registry = new SimpleSerializerRegistry();
    }

    @Test
    public void testFallback() {
        assertThrows(CatCodes.InvalidCatCodeException.class, () -> ((IComponentSerializer) registry).deserialize(null));
        assertThrows(CatCodes.InvalidCatCodeException.class, () -> ((IComponentSerializer) registry).deserialize(CatCodes.newCatCode("a")));
    }

    @Test
    public void testRegistration() {
        var regularSerializer = new RegularComponentSerializer();
        registry.registerComponentSerializer("TEXT", regularSerializer);
        assertSame(registry.getComponentSerializer("TEXT").get(), regularSerializer);
        var code = "[dash:TEXT,content=114514]";
        assertEquals(((IComponentSerializer)registry).deserialize(CatCodes.fromString(code).get(0)).toCatCode(),"114514");
    }
}