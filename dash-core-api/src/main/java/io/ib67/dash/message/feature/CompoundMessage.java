package io.ib67.dash.message.feature;

import io.ib67.dash.message.Message;
import io.ib67.dash.message.MessageChain;
import io.ib67.dash.message.MessageSource;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

/**
 * A {@link CompoundMessage} is a set of {@link MessageComponent}, which is mutable.
 *
 * @param <S>
 */
@ApiStatus.AvailableSince("0.1.0")
public class CompoundMessage<S extends MessageSource> extends Message<S> {
    /**
     * The content of {@link CompoundMessage}, aka "MessageChain"
     */
    @Getter
    private final MessageChain components;

    public CompoundMessage(S source, MessageChain components) {
        super(source);
        Objects.requireNonNull(this.components = components);
    }


}
