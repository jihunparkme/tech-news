package com.technews.common.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Result {
    FAIL("FAIL"),
    SUCCESS("SUCCESS");

    private final String message;

    public String message() {
        return message;
    }
}
