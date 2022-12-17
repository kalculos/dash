package io.ib67.dash.message;

import io.ib67.dash.adapter.PlatformRelated;

/**
 * The source of {@link AbstractMessage}.
 * Can be anything, depending on its subclasses.
 */
public interface IMessageSource extends PlatformRelated {
    //todo getPlatform so that users can know more about their message sauce.
    IMessageReceipt sendMessage(MessageChain message);

    IMessageReceipt reply(long messageId, MessageChain chain);

    IMessageReceipt recall(long messageId);
}
