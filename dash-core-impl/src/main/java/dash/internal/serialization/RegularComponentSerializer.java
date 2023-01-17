package dash.internal.serialization;

import dash.internal.DashImpl;
import io.ib67.dash.message.feature.IComponentSerializer;
import io.ib67.dash.message.feature.IMessageComponent;
import io.ib67.dash.message.feature.component.*;
import io.ib67.dash.util.CatCodes;
import io.ib67.kiwi.Kiwi;
import org.jetbrains.annotations.NotNull;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class RegularComponentSerializer implements IComponentSerializer {
    /**
     * @implNote while a new parser strategy is added, also register it on {@link DashImpl#registerStandardCatCodes()}
     * @param code
     * @return
     * @throws CatCodes.InvalidCatCodeException
     */
    @Override
    public @NotNull IMessageComponent deserialize(CatCodes.CatCode code) throws CatCodes.InvalidCatCodeException {
        return switch (code.getType().toUpperCase()) {
            case "TEXT" -> new Text(code.getProp(CatCodes.CatCode.TEXT_PROP_KEY));
            case "AT" -> {
                if (code.getProp("platform") != null) {
                    yield new At(null, code.getProp("platform"), code.getProp("display"), code.getProp("target"));
                }
                yield Kiwi.todo("convert to contact");
            }
            case "IMAGE" -> Image.builder().path(getPath(code)).build();
            case "FILE" -> File.builder().path(getPath(code)).build();
            case "ACTION" -> new Action(Action.Type.valueOf(code.getProp("type")));
            case "AUDIO" -> Audio.builder().path(getPath(code)).build();
            case "STICKER" -> new Sticker(code.getProp("path") != null ? getPath(code) : null
                    , code.getProp("platform"), Integer.parseInt(code.getProp("id")));
            default -> throw new IllegalArgumentException("Unknown catcode type for RegularComponentSerializer, this is unexpected.");
        };
    }

    private Path getPath(CatCodes.CatCode code) {
        return Path.of(URLDecoder.decode(code.getProp("path"), StandardCharsets.UTF_8));
    }
}
