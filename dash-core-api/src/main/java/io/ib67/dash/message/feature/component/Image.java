package io.ib67.dash.message.feature.component;

import io.ib67.dash.message.feature.IMessageComponent;
import lombok.Builder;
import org.jetbrains.annotations.ApiStatus;

import java.nio.file.Path;

/**
 * A {@link Image} is a concrete implementation of {@link IMessageComponent} that contains photo.
 */
@ApiStatus.AvailableSince("0.1.0")
public class Image extends ResourceMessage implements IMessageComponent {

    @Builder
    protected Image(Path path) {
        super("IMAGE", path);
    }

    @Override
    public String toString() {
        return "[\uD83D\uDDBC️]"; //🖼️
    }
}
