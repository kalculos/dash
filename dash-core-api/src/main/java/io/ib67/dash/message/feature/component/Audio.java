package io.ib67.dash.message.feature.component;

import io.ib67.dash.message.feature.MessageComponent;
import org.jetbrains.annotations.ApiStatus;

/**
 * An {@link Audio} is a concrete implementation of {@link io.ib67.dash.message.feature.MessageComponent} that contains audio / voice.
 */
@ApiStatus.AvailableSince("0.1.0")
public class Audio extends ResourceMessage implements MessageComponent {


    protected Audio(String path) {
        super("AUDIO", path);
    }

    @Override
    public String toString() {
        return "[\uD83D\uDD0A]"; //🔊
    }
}
