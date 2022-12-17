package io.ib67.dash.message;

import io.ib67.dash.message.feature.ComponentSerializer;
import io.ib67.dash.message.feature.MessageComponent;
import io.ib67.dash.util.CatCodes;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A set of messages.
 */
@ApiStatus.AvailableSince("0.1.0")
public class MessageChain extends ArrayList<MessageComponent> {
    public MessageChain(int initialCapacity) {
        super(initialCapacity);
    }

    public MessageChain() {
        super();
    }

    public MessageChain(@NotNull Collection<? extends MessageComponent> c) {
        super(c);
    }

    /**
     * Parse some CatCode.
     *
     * @param serializer components' serializer
     * @param str        catcodes
     * @param lenient    skip illegal catcodes
     * @return MessageChain
     */
    @NotNull
    public static MessageChain fromCatCode(ComponentSerializer serializer, boolean lenient, List<CatCodes.CatCode> str) {
        var newChain = new MessageChain(str.size());
        for (CatCodes.CatCode catCode : str) {
            if (lenient) {
                try {
                    newChain.add(serializer.parse(catCode));
                } catch (CatCodes.InvalidCatCodeException exception) {
                    exception.printStackTrace();
                }
            } else {
                newChain.add(serializer.parse(catCode));
            }
        }
        return newChain;
    }

    public static MessageChain fromCatCode(ComponentSerializer serializer, List<CatCodes.CatCode> str) {
        return fromCatCode(serializer, true, str);
    }

    public static MessageChain fromCatCode(ComponentSerializer serializer, String str) {
        return fromCatCode(serializer, CatCodes.fromString(str));
    }

    public static MessageChain fromCatCode(ComponentSerializer serializer, boolean lenient, String str) {
        return fromCatCode(serializer, lenient, CatCodes.fromString(str));
    }

    /**
     * Generates a human-readable String.<br />
     * This will drop some information that cannot be transformed into String, such as Image.
     *
     * @return A human-readable String.
     */
    @Contract("-> new")
    public String getDisplay() {
        return stream().map(Object::toString).collect(Collectors.joining());
    }

    /**
     * Generate a {@link CatCodes.CatCode}
     *
     * @return catcode
     */
    public String contentToString() {
        return stream().map(MessageComponent::toCatCode).collect(Collectors.joining());
    }
}
