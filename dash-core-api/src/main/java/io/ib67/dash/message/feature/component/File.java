package io.ib67.dash.message.feature.component;

import io.ib67.dash.message.feature.IMessageComponent;
import lombok.Builder;
import org.jetbrains.annotations.ApiStatus;

import java.nio.file.Path;

/**
 * A {@link File} is a concrete implementation of {@link IMessageComponent} that contains a path to read file.
 */
@ApiStatus.AvailableSince("0.1.0")
public class File extends ResourceMessage implements IMessageComponent {
    @Builder
    protected File(Path path) {
        super("FILE", path);
    }

    @Override
    public String toString() {
        return "[\uD83D\uDCCE]"; //📎
    }
}
