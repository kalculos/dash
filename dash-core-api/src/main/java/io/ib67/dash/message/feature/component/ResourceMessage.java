package io.ib67.dash.message.feature.component;

import io.ib67.dash.message.feature.IMessageComponent;
import io.ib67.dash.util.CatCodes;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.ApiStatus;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * A {@link ResourceMessage} is a {@link IMessageComponent} that is linked to external resources.
 */
@ApiStatus.AvailableSince("0.1.0")
public abstract class ResourceMessage implements IMessageComponent {
    private final String type;
    @Getter
    private final Path path;

    protected ResourceMessage(String type, Path path) {
        this.type = type;
        this.path = path;
    }

    @SneakyThrows
    public InputStream openInputStream() {
        return path.toUri().toURL().openStream();
    }

    @Override
    public String toCatCode() {
        return CatCodes.ofProps(
                "path", path.toString()
        ).type(type).toString();
    }
}
