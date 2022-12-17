package io.ib67.dash.message.feature.component;

import io.ib67.dash.message.feature.MessageComponent;
import org.jetbrains.annotations.ApiStatus;

/**
 * A {@link Image} is a concrete implementation of {@link MessageComponent} that contains photo.
 */
@ApiStatus.AvailableSince("0.1.0")
public class Image extends ResourceMessage implements MessageComponent {

    protected Image(String path) {
        super("IMAGE", path);
    }

    @Override
    public String toString() {
        return "[\uD83D\uDDBC️]"; //🖼️
    }
}
