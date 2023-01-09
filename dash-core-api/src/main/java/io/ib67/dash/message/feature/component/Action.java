package io.ib67.dash.message.feature.component;

import io.ib67.dash.message.feature.IMessageComponent;
import io.ib67.dash.util.CatCodes;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * An {@link Action} is a concrete implementation of {@link IMessageComponent} that contains the type of the action.
 * It depends on platform. todo Currently, we don't have a universal parser for it
 */
@RequiredArgsConstructor
@Getter
public class Action implements IMessageComponent {
    private final Type type;

    @Override
    public String toCatCode() {
        return CatCodes.ofProps(
                "type", type.toString()
        ).type("ACTION").toString();
    }

    @Override
    public String toString() {
        return "";
    }

    public enum Type {
        QUIT, MUTED
    }
}
