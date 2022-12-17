package io.ib67.dash.message.feature.component;

import io.ib67.dash.message.feature.MessageComponent;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;

/**
 * A {@link Text} is a {@link MessageComponent} that only contains text.
 */
@RequiredArgsConstructor
@ApiStatus.AvailableSince("0.1.0")
public class Text implements MessageComponent {
    private final String content;

    @Override
    public String toCatCode() {
        return content;
    }

    @Override
    public String toString() {
        return content;
    }
}
