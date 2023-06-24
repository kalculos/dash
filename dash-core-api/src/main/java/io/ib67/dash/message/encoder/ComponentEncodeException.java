package io.ib67.dash.message.encoder;

import io.ib67.dash.message.feature.IMessageComponent;
import lombok.Generated;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

/**
 * Thrown if there are some components that cannot be encoded.
 */
@ApiStatus.AvailableSince("0.1.0")
public class ComponentEncodeException extends RuntimeException{
    /**
     * The involved component.
     */
    @Getter
    private final IMessageComponent component;

    @Generated
    public ComponentEncodeException(IMessageComponent component) {
        super();
        this.component = component;
    }

    @Generated
    public ComponentEncodeException(String message, IMessageComponent component) {
        super(message);
        this.component = component;
    }

    @Generated
    public ComponentEncodeException(String message, Throwable cause, IMessageComponent component) {
        super(message, cause);
        this.component = component;
    }

    @Generated
    public ComponentEncodeException(Throwable cause, IMessageComponent component) {
        super(cause);
        this.component = component;
    }

    @Generated
    protected ComponentEncodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, IMessageComponent component) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.component = component;
    }
}
