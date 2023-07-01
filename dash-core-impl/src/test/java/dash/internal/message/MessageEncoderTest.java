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

package dash.internal.message;

import io.ib67.dash.message.MessageChain;
import io.ib67.dash.message.encoder.IMessageEncoder;
import io.ib67.dash.message.feature.CompoundMessage;
import io.ib67.dash.message.feature.component.File;
import io.ib67.dash.message.feature.component.Image;
import io.ib67.dash.message.feature.component.Text;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class MessageEncoderTest {
    private IMessageEncoder.Builder builder;

    @BeforeEach
    public void setup() {
        builder = new MessageEncoderImpl.BuilderImpl(new HashMap<>()).register(Image.class, (a, b, c) -> b.append(a.toString()).ret(true));
    }

    @Test
    public void testRegister() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> builder.register(Text.class, null));
        assertTrue(builder.register(File.class, (a, b, c) -> b.append(a.toString()).ret(true)).build().encodeSingle(File.builder().build()).get(0) instanceof Text);
    }

    @Test
    public void testClear() {
        builder.clear();
        assertSame(builder.build().encodeSingle(Image.builder().build()).get(0).getClass(), Image.class);
    }

    @Test
    public void testKeep() {
        assertTrue(builder.register(File.class, (a, b, c) -> b.append(a.toString()).ret(true))
                .keep(File.class).build().encodeSingle(File.builder().build()).get(0) instanceof File);
    }

    @Test
    public void testEncodeByMessage(){
        var message = new CompoundMessage<>(null,0, MessageChain.of(Image.builder().build()));
        var encoder = builder.build();
        assertTrue(encoder.encodeByMessage(message).get(0) instanceof Text);
    }

    @Test
    public void testClone(){
        var encoder = builder.build();
        var clone = encoder.cloneEncoder();
        assertNotSame(encoder,clone);
    }
}
