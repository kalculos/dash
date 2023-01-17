package io.ib67.dash.message.feature.component;

import io.ib67.dash.util.CatCodes;
import lombok.Builder;
import lombok.Getter;

import java.nio.file.Path;

@Getter
public class Sticker extends ResourceMessage {
    private final String platform;

    private final int id;

    @Builder
    public Sticker(Path path, String platform, int id) {
        super("STICKER",path);
        this.platform = platform;
        this.id = id;
    }

    @Override
    public String toString() {
        return "[\uD83E\uDD74]"; //🥴
    }
    @Override
    public String toCatCode() {
        return CatCodes.ofProps(
                "path", super.getPath().toString(),
                "platform",platform,
                "id",id+""
        ).type("STICKER").toString();
    }
}
