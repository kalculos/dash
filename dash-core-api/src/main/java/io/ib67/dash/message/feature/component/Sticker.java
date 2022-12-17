package io.ib67.dash.message.feature.component;

public class Sticker extends Image {
    private final String platform;
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
