package io.ib67.dash.message.feature.component;

import io.ib67.dash.message.feature.IMessageComponent;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;

/**
 * A {@link Text} is a {@link IMessageComponent} that only contains text.
 */
@RequiredArgsConstructor
@ApiStatus.AvailableSince("0.1.0")
public class Text implements IMessageComponent {
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
