package io.ib67.dash.message.feature.component;

import io.ib67.dash.message.feature.MessageComponent;
import io.ib67.dash.util.CatCodes;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.ApiStatus;

import java.io.InputStream;
import java.net.URL;

@ApiStatus.AvailableSince("0.1.0")
public abstract class ResourceMessage implements MessageComponent {
    private final String type;
    @Getter
    private final String path;

    protected ResourceMessage(String type, String path) {
        this.type = type;
        this.path = path;
    }

    @SneakyThrows
    public InputStream openInputStream() {
        return new URL(path).openStream();
    }

    @Override
    public String toCatCode() {
        return CatCodes.of(
                "path", path
        ).type(type).toString();
    }
}
