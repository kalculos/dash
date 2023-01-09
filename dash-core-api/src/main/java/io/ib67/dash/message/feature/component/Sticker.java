package io.ib67.dash.message.feature.component;

import lombok.Getter;

import java.nio.file.Path;

@Getter
public class Sticker extends Image {
    private final String platform;

    private final int id;

    public Sticker(Path path, String platform, int id) {
        super(path);
        this.platform = platform;
        this.id = id;
    }

    @Override
    public String toString() {
        return "[\uD83E\uDD74]"; //🥴
    }
}
