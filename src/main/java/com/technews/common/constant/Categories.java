package com.technews.common.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Categories {
    SPRING_FRAMEWORK("spring-framework", "/category/spring-framework"),
    SPRING_BOOT("spring-boot", "/category/spring-boot"),
    SPRING_DATA_JPA("spring-data-jpa", "/category/spring-data-jpa"),
    SPRING_BATCH("spring-batch", "/category/spring-batch"),
    ;

    private String value;
    private String url;

    public String value() {
        return value;
    }

    public String url() {
        return url;
    }
}
