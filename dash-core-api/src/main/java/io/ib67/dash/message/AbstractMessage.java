package io.ib67.dash.message;

import io.ib67.dash.adapter.PlatformAdapter;
import io.ib67.dash.adapter.PlatformRelated;
import io.ib67.dash.event.AbstractEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link AbstractMessage} is an object that is composed of {@code context}, {@code sender} and {@code content}. <br />
 * The {@code context} enables you to mark or attach your data on this message (in its lifecycle). <br />
 * By using {@code context}, you can share data between interrupters and handlers.  <br />
 * <p>
 * Warning: The {@code context} is NOT thread-safe.
 */
@ApiStatus.AvailableSince("0.1.0")
@RequiredArgsConstructor
public abstract class AbstractMessage<S extends IMessageSource> extends AbstractEvent implements PlatformRelated {
    /**
     * The source of the message.
     */
    protected final S source;

    /**
     * The context of message, created lazily by default.
     */
    @Getter(lazy = true)
    private final Context context = new Context();

    /**
     * Universal message id.
     */
    @Getter
    private final long id;

    /**
     * To quickly reply
     *
     * @param message response
     * @return if succeed
     */
    public boolean reply(MessageChain message) {
        return source.reply(id, message).isSent();
    }

    @Override
    public PlatformAdapter getPlatform() {
        return source.getPlatform();
    }

    public static class Context {
        private Map<Enum<?>, Object> context = new HashMap<>();

        /**
         * Gets a nullable data from context map.<br />
         * Example: {@code context.<Order>getAs(KEY_USER_ORDER);}
         *
         * @param enu enum
         * @param <T> type of data.
         * @return data, maybe null
         */
        @SuppressWarnings("unchecked")
        public <T> T getAs(Enum<?> enu) {
            return (T) context.get(enu);
        }

        /**
         * Some boilerplate.
         **/
        public String getString(Enum<?> enu) {
            return this.getAs(enu);
        }

        public int getInt(Enum<?> enu) {
            return getAs(enu);
        }

        public Object get(Enum<?> enu) {
            return getAs(enu);
        }
    }

    @Override
    public int hashCode() {
        int i = 1;
        i = i * 37 + source.hashCode();
        i = i * 31 + Long.hashCode(id);
        return i;
    }
}
