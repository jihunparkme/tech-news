package com.technews.aggregate.releases.springframework.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Categories {
    SPRING_FRAMEWORK("spring-framework", "https://github.com/spring-projects/spring-framework/tags"),
    SPRING_BOOT("spring-boot", "https://github.com/spring-projects/spring-boot/tags"),
    SPRING_DATA_JPA("spring-data-jpa", "https://github.com/spring-projects/spring-data-jpa/tags"),
    SPRING_BATCH("spring-batch", "https://github.com/spring-projects/spring-batch/tags"),

    JDK8("JDK 8", "https://www.oracle.com/java/technologies/javase/8u-relnotes.html"),
    JDK11("JDK 11", "https://www.oracle.com/java/technologies/javase/11u-relnotes.html"),
    JDK17("JDK 17", "https://www.oracle.com/java/technologies/javase/17u-relnotes.html"),
    JDK21("JDK 21", "https://www.oracle.com/java/technologies/javase/21u-relnotes.html"),
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
