package com.technews.common.exception;

public class NotFoundSubscribe extends RuntimeException {
    public NotFoundSubscribe() {
        super("Not Found Subscribe");
    }

    public NotFoundSubscribe(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundSubscribe(String message) {
        super(message);
    }

    public NotFoundSubscribe(Throwable cause) {
        super(cause);
    }
}