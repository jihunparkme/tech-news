package com.technews.aggregate.posts.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BlogSubjects {
    SPRING("Spring"),
    JAVA("Java"),
    ;

    private String value;

    public String value() {
        return value;
    }
}
