package io.ib67.dash.message.feature;

import io.ib67.dash.message.AbstractMessage;
import io.ib67.dash.message.IMessageSource;
import io.ib67.dash.message.MessageChain;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

/**
 * A {@link CompoundMessage} is a set of {@link IMessageComponent}, which is mutable.
 *
 * @param <S>
 */
@ApiStatus.AvailableSince("0.1.0")
@Getter
public class CompoundMessage<S extends IMessageSource> extends AbstractMessage<S> {
    /**
     * The content of {@link CompoundMessage}, aka "MessageChain"
     */
    private final MessageChain components;

    public CompoundMessage(S source, long id, MessageChain components) {
        super(source, id);
        Objects.requireNonNull(this.components = components);
    }

    public boolean containString(String str) {
        return components.containString(str);
    }

}
