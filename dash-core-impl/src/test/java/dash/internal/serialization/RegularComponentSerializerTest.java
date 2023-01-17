package dash.internal.serialization;

import io.ib67.dash.message.feature.component.*;
import io.ib67.dash.util.CatCodes;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegularComponentSerializerTest {
    private static final RegularComponentSerializer serializer = new RegularComponentSerializer();

    @Test
    public void testPlain(){
        var c = serializer.deserialize(CatCodes.newCatCode("TEXT")
                .prop(CatCodes.CatCode.TEXT_PROP_KEY,"lol"));
        assertTrue(c instanceof Text);
        assertEquals("lol",c.toString());
    }

    @Test
    public void testAt(){
        var at = At.builder()
                .platformId("platform")
                .display("display")
                .platformUid("uid").build();
        var _at = (At) serializer.deserialize(CatCodes.fromString("[dash:AT,platform=platform,display=display,target=uid]").get(0));
        assertEquals(at.platformId(),_at.platformId(),"platform id");
        assertEquals(at.display(),_at.display(),"display");
        assertEquals(at.platformUid(),_at.platformUid(),"platform uid");
    }

    @Test
    public void testImage(){
        var img = Image.builder()
                .path(Path.of("dash://image/111"))
                .build();
        var _img = (Image) serializer.deserialize(CatCodes.fromString(img.toCatCode()).get(0));
        assertEquals(_img.getPath().toString(),img.getPath().toString());
    }

    @Test
    public void testFile(){
        var img = File.builder()
                .path(Path.of("dash://image/111"))
                .build();
        var _img = (File) serializer.deserialize(CatCodes.fromString(img.toCatCode()).get(0));
        assertEquals(_img.getPath().toString(),img.getPath().toString());
    }
    @Test
    public void testAudio(){
        var img = Audio.builder()
                .path(Path.of("dash://image/111"))
                .build();
        var _img = (Audio) serializer.deserialize(CatCodes.fromString(img.toCatCode()).get(0));
        assertEquals(_img.getPath().toString(),img.getPath().toString());
    }
    @Test
    public void testSticker(){
        var img = Sticker.builder()
                .path(Path.of("dash://sticker/111"))
                .platform("platform")
                .id(1).build();
        var _img = (Sticker) serializer.deserialize(CatCodes.fromString(img.toCatCode()).get(0));
        assertEquals(_img.getPath().toString(),img.getPath().toString());
        assertEquals(_img.getPlatform(),img.getPlatform());
        assertEquals(_img.getId(),img.getId());
    }
}