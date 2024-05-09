package com.technews.common.constant;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum SpringRepository {
    SPRING_FRAMEWORK("spring-framework"),
    SPRING_BOOT("spring-boot"),
    SPRING_DATA_JPA("spring-data-jpa"),
    SPRING_BATCH("spring-batch"),
    ;

    private String value;

    public static List<String> list() {
        return Arrays.stream(values())
                .map(repository -> repository.value)
                .collect(Collectors.toUnmodifiableList());
    }

    public String value() {
        return value;
    }
}
