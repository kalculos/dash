package io.ib67.dash.message.feature.component;

import io.ib67.dash.message.feature.IMessageComponent;
import lombok.Builder;
import org.jetbrains.annotations.ApiStatus;

import java.nio.file.Path;

/**
 * An {@link Audio} is a concrete implementation of {@link IMessageComponent} that contains audio / voice.
 */
@ApiStatus.AvailableSince("0.1.0")
public class Audio extends ResourceMessage implements IMessageComponent {
    @Builder
    protected Audio(Path path) {
        super("AUDIO", path);
    }

    @Override
    public String toString() {
        return "[\uD83D\uDD0A]"; //🔊
    }
}
