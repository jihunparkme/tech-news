package com.technews.common.constant;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum JdkVersion {
    JDK_8("JDK 8", "https://www.oracle.com/java/technologies/javase/8u-relnotes.html"),
    JDK_11("JDK 11", "https://www.oracle.com/java/technologies/javase/11u-relnotes.html"),
    JDK_17("JDK 17", "https://www.oracle.com/java/technologies/javase/17u-relnotes.html"),
    JDK_21("JDK 21", "https://www.oracle.com/java/technologies/javase/21u-relnotes.html"),
    ;

    private String value;
    private String url;

    public String value() {
        return value;
    }

    public String url() {
        return url;
    }

    public static List<String> list() {
        return Arrays.stream(values())
                .map(jdk -> jdk.value)
                .collect(Collectors.toUnmodifiableList());
    }
}