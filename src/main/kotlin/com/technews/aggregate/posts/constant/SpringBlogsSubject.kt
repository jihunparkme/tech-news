package com.technews.aggregate.posts.constant

enum class SpringBlogsSubject(val value: String) {
    ENGINEERING("engineering"),
    RELEASES("releases"),
    NEWS("news"),
    ;

    companion object {
        fun from(value: String): SpringBlogsSubject {
            return entries.firstOrNull { it.value == value } ?: ENGINEERING
        }
    }
}
