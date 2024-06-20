package com.technews.aggregate.posts.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum JavaBlogsSubject {
    INSIDE("inside"),
    ORACLE("oracle"),
    ;

    private String value;

    public String value() {
        return value;
    }
}
