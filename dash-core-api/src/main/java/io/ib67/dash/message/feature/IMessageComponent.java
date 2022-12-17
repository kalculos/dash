package io.ib67.dash.message.feature;

/**
 * A part of a message, {@link io.ib67.dash.message.feature.CompoundMessage}.
 *
 * @implSpec A human-readable {@code toString} must be implemented.
 */
public interface IMessageComponent {
    String toCatCode();
}
