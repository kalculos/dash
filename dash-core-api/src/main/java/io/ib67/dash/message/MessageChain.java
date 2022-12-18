package io.ib67.dash.message;

import io.ib67.dash.Dash;
import io.ib67.dash.message.feature.IComponentSerializer;
import io.ib67.dash.message.feature.IMessageComponent;
import io.ib67.dash.message.feature.component.Text;
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
public class MessageChain extends ArrayList<IMessageComponent> {
    public MessageChain(int initialCapacity) {
        super(initialCapacity);
    }

    public MessageChain() {
        super();
    }

    public MessageChain(@NotNull Collection<? extends IMessageComponent> c) {
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
    public static MessageChain fromCatCode(IComponentSerializer serializer, boolean lenient, List<CatCodes.CatCode> str) {
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

    public static MessageChain fromCatCode(IComponentSerializer serializer, List<CatCodes.CatCode> str) {
        return fromCatCode(serializer, true, str);
    }

    public static MessageChain fromCatCode(IComponentSerializer serializer, String str) {
        return fromCatCode(serializer, CatCodes.fromString(str));
    }

    public static MessageChain fromCatCode(IComponentSerializer serializer, boolean lenient, String str) {
        return fromCatCode(serializer, lenient, CatCodes.fromString(str));
    }

    public static MessageChain fromCatCode(boolean lenient, List<CatCodes.CatCode> str) {
        return fromCatCode(Dash.getInstance().defaultComponentSerializer(), lenient, str);
    }

    public static MessageChain fromCatCode(List<CatCodes.CatCode> str) {
        return fromCatCode(Dash.getInstance().defaultComponentSerializer(), true, str);
    }

    public static MessageChain fromCatCode(String str) {
        return fromCatCode(Dash.getInstance().defaultComponentSerializer(), str);
    }

    public static MessageChain fromCatCode(boolean lenient, String str) {
        return fromCatCode(Dash.getInstance().defaultComponentSerializer(), lenient, str);
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
        return stream().map(IMessageComponent::toCatCode).collect(Collectors.joining());
    }

    /**
     * An efficient implementation of searching substrings.
     *
     * @param str target
     * @return if exists
     */
    public boolean containString(String str) {
        return stream().anyMatch(it -> {
            if (it instanceof Text text) {
                return text.toString().contains(str);
            }
            return false;
        });
    }

    /**
     * A safer implementation of replaceAll.
     *
     * @return a new message chain as result
     */
    @Contract("_, _ -> new")
    public MessageChain replaceAll(String regex, String toBe) {
        var newChain = new MessageChain(size());
        stream().map(it -> performReplaceAll(regex, toBe, it)).forEach(newChain::add);
        return newChain;
    }

    @SuppressWarnings("unchecked")
    private <T extends IMessageComponent> T performReplaceAll(String regex, String toBe, T it) {
        if (it instanceof Text t) {
            return (T) new Text(t.toString().replaceAll(regex, toBe)); // we can make sure that T is Text
        }
        return it;
    }

    public boolean contains(String str) {
        return containString(str);
    }

    public MessageChain append(IMessageComponent component) {
        add(component);
        return this;
    }
}
