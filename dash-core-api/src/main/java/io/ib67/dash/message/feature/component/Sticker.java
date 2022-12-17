package io.ib67.dash.message.feature.component;

import lombok.Getter;

public class Sticker extends Image {
    @Getter
    private final String platform;
    @Getter
    private final int id;

    protected Sticker(String path, String platform, int id) {
        super(path);
        this.platform = platform;
        this.id = id;
    }

    @Override
    public String toString() {
        return "[\uD83E\uDD74]"; //🥴
    }
}
