package io.ib67.dash.message;

import io.ib67.dash.adapter.PlatformAdapter;
import io.ib67.dash.adapter.PlatformRelated;
import io.ib67.dash.event.AbstractEvent;
import io.ib67.dash.event.ContextualEvent;
import io.ib67.dash.message.internal.UnorderedMessageContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * A {@link AbstractMessage} is an object that is composed of {@code context}, {@code sender} and {@code content}. <br />
 * The {@code context} enables you to mark or attach your data on this message (in its lifecycle). <br />
 * By using {@code context}, you can share data between interrupters and handlers.  <br />
 * <p>
 * Warning: The {@code context} is NOT thread-safe.
 */
@ApiStatus.AvailableSince("0.1.0")
@RequiredArgsConstructor
public abstract class AbstractMessage<S extends IMessageSource> extends AbstractEvent implements PlatformRelated, ContextualEvent {
    /**
     * The source of the message.
     */
    @Getter
    protected final S source;

    /**
     * The context of the message
     */
    @Getter
    private final IMessageContext context = new UnorderedMessageContext();

    /**
     * An universal message id.
     */
    @Getter
    protected final long id;

    /**
     * Some utility for writing reply codes quickly
     *
     * @param message response
     * @return if succeed
     */
    public boolean reply(MessageChain message) {
        return source.reply(id, message).isSent();
    }

    public boolean reply(String mesg) {
        return reply(MessageChain.fromCatCode(mesg));
    }

    @Override
    public PlatformAdapter getPlatform() {
        return source.getPlatform();
    }

    @Override
    public boolean hasContext(@Nullable ContextKey<?> key){
        return context.has(requireNonNull(key));
    }

    @Override
    public int hashCode() {
        int i = 1;
        i = i * 37 + source.hashCode();
        i = i * 31 + Long.hashCode(id);
        return i;
    }
}
