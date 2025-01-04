package com.technews.common.dto

enum class Project(val value: String) {
    SPRING("Spring"),
    JAVA("Java"),
    KAFKA("kafka"),
    ;

    companion object {
        fun from(value: String): Project {
            return entries.firstOrNull { it.value == value } ?: SPRING
        }
    }
}
