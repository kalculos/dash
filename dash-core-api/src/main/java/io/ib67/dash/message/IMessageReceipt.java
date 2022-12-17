package io.ib67.dash.message;

import org.jetbrains.annotations.ApiStatus;

/**
 * A {@link IMessageReceipt} allows you to control sent messages more fluently.
 */
@ApiStatus.AvailableSince("0.1.0")
public interface IMessageReceipt {
    /**
     * Recall your message, can be failed.
     *
     * @return if succeed
     */
    boolean tryRecall();

    /**
     * Edit your message, can be failed
     *
     * @param newContent new content
     * @return if succeed
     */
    boolean tryEdit(MessageChain newContent);

    /**
     * To check if your bot is muted, kicked or the contact isn't exists.
     *
     * @return if the message is sent successfully.
     */
    boolean isSent();
}
