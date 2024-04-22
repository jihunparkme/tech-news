package com.technews.common.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SpringRepository {
    SPRING_FRAMEWORK("spring-framework"),
    SPRING_BOOT("spring-boot"),
    SPRING_DATA_JPA("spring-data-jpa"),
    SPRING_BATCH("spring-batch"),
    ;

    private String value;

    public String value() {
        return value;
    }
}
