package io.ib67.dash.exception;

import io.ib67.dash.contact.group.Member;

public class NotFriendException extends RuntimeException {
    private final Member member;

    public NotFriendException(Member member) {
        super();
        this.member = member;
    }

    public NotFriendException(String message, Member member) {
        super(message);
        this.member = member;
    }

    public NotFriendException(String message, Throwable cause, Member member) {
        super(message, cause);
        this.member = member;
    }

    public NotFriendException(Throwable cause, Member member) {
        super(cause);
        this.member = member;
    }

    protected NotFriendException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Member member) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.member = member;
    }
}
