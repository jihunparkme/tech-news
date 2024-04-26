package com.technews.aggregate.releases.springframework.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Categories {
    SPRING_FRAMEWORK("spring-framework"),
    SPRING_BOOT("spring-boot"),
    SPRING_DATA_JPA("spring-data-jpa"),
    SPRING_BATCH("spring-batch"),

    JDK8("jdk8"),
    JDK11("jdk11"),
    JDK17("jdk17"),
    JDK21("jdk21"),
    ;

    private String value;

    public String value() {
        return value;
    }
}
