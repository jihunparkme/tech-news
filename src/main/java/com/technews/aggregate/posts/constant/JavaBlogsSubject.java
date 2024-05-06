package com.technews.aggregate.posts.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum JavaBlogsSubject {
    INSIDE("inside"),
    ;

    private String value;

    public String value() {
        return value;
    }
}
