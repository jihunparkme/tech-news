package com.technews.aggregate.posts.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SpringBlogsSubject {
    ENGINEERING("engineering"),
    RELEASES("releases"),
    NEWS("news"),
    ;

    private String value;

    public String value() {
        return value;
    }
}
