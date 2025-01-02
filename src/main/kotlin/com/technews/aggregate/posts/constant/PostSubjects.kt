package com.technews.aggregate.posts.constant

enum class PostSubjects(val value: String) {
    SPRING("Spring"),
    JAVA("Java"),
    KAFKA("kafka"),
    ;

    companion object {
        fun from(value: String): PostSubjects {
            return entries.firstOrNull { it.value == value } ?: SPRING
        }
    }
}
