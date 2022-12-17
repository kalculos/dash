package io.ib67.dash.message.feature.component;

import io.ib67.dash.message.feature.MessageComponent;
import org.jetbrains.annotations.ApiStatus;

/**
 * A {@link File} is a concrete implementation of {@link MessageComponent} that contains a path to read file.
 */
@ApiStatus.AvailableSince("0.1.0")
public class File extends ResourceMessage implements MessageComponent {


    protected File(String path) {
        super("FILE", path);
    }

    @Override
    public String toString() {
        return "[\uD83D\uDCCE]"; //📎
    }
}
