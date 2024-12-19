package com.technews.common.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Collections {
    RELEASES("spring-projects", "RELEASES"),
    ;

    private String group;
    private String key;

    public String group() {
        return group;
    }

    public String key() {
        return key;
    }
}
