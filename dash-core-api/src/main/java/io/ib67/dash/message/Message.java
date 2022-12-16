package io.ib67.dash.message;

import io.ib67.dash.event.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link Message} is an object that is composed of {@code context}, {@code sender} and {@code content}. <br />
 * The {@code context} enables you to mark or attach your data on this message (in its lifecycle). <br />
 * By using {@code context}, you can share data between interrupters and handlers.  <br />
 * <p>
 * Warning: The {@code context} is NOT thread-safe.
 */
@ApiStatus.AvailableSince("0.1.0")
@RequiredArgsConstructor
public abstract class Message extends Event {
    @Getter
    protected final MessageSource source;
    @Getter(lazy = true)
    private final Context context = new Context();

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
}
