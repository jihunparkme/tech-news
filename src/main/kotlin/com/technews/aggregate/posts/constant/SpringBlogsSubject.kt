package com.technews.aggregate.posts.constant

enum class SpringBlogsSubject(val value: String) {
    ENGINEERING("Engineering"),
    RELEASES("Releases"),
    NEWS("News"),
    ;

    companion object {
        fun from(value: String): SpringBlogsSubject {
            return entries.firstOrNull { it.value == value } ?: ENGINEERING
        }
    }
}
