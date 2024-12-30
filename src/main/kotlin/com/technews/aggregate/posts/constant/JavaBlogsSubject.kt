package com.technews.aggregate.posts.constant

enum class JavaBlogsSubject(val value: String) {
    INSIDE("inside"),
    ORACLE("oracle"),
    ;

    companion object {
        fun from(value: String): JavaBlogsSubject {
            return entries.firstOrNull { it.value == value } ?: INSIDE
        }
    }
}
