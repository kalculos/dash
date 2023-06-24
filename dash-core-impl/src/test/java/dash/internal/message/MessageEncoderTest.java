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

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertTrue(builder.build().encodeSingle(Image.builder().build()).get(0).getClass() == Image.class);
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
