package io.ib67.dash.message.feature.component;

import io.ib67.dash.contact.Contact;
import io.ib67.dash.message.feature.IMessageComponent;
import io.ib67.dash.util.CatCodes;
import lombok.Builder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Mention a user.
 * Also see: {@link #unsafeAt(String, String)}
 * @param contact determines the universal user id.
 */
@ApiStatus.AvailableSince("0.1.0")
public record At(
        @Nullable Contact contact,
        @Nullable String platformId,
        @Nullable String display,
        @Nullable String platformUid
) implements IMessageComponent {
    /**
     * Create a new {@link At} directly
     *
     * @param platformId
     * @param platformUid
     * @return
     */
    public static At unsafeAt(String platformId, String platformUid) {
        return new At(null, platformId, platformUid, null);
    }

    @Builder
    public At {
    }

    @Override
    public String toCatCode() {
        if (contact != null) {
            return CatCodes.ofProps("target", String.valueOf(contact.getUid())).type("AT").toString();
        }
        return CatCodes.ofProps(
                "platform", platformId,
                "target", platformUid
        ).type("AT").toString();
    }

    @Override
    public String toString() {
        return "@" + contact.getName() + " ";
    }
}
