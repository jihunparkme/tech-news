package com.technews.aggregate.releases.springframework.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Categories {
    SPRING_FRAMEWORK("spring-framework"),
    SPRING_BOOT("spring-boot"),
    SPRING_DATA_JPA("spring-data-jpa"),
    SPRING_BATCH("spring-batch"),

    JDK8("JDK 8"),
    JDK11("JDK 11"),
    JDK17("JDK 17"),
    JDK21("JDK 21"),
    ;

    private String value;

    public String value() {
        return value;
    }
}
