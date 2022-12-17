package io.ib67.dash.message.feature.component;

import io.ib67.dash.message.feature.IMessageComponent;
import io.ib67.dash.util.CatCodes;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.ApiStatus;

import java.io.InputStream;
import java.net.URL;

/**
 * A {@link ResourceMessage} is a {@link IMessageComponent} that is linked to external resources.
 */
@ApiStatus.AvailableSince("0.1.0")
public abstract class ResourceMessage implements IMessageComponent {
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
