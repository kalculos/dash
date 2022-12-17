package io.ib67.dash.exception;

import io.ib67.dash.contact.group.AbstractMember;

public class NotFriendException extends RuntimeException {
    private final AbstractMember member;

    public NotFriendException(AbstractMember member) {
        super();
        this.member = member;
    }

    public NotFriendException(String message, AbstractMember member) {
        super(message);
        this.member = member;
    }

    public NotFriendException(String message, Throwable cause, AbstractMember member) {
        super(message, cause);
        this.member = member;
    }

    public NotFriendException(Throwable cause, AbstractMember member) {
        super(cause);
        this.member = member;
    }

    protected NotFriendException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, AbstractMember member) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.member = member;
    }
}
