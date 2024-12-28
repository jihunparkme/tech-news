package com.technews.aggregate.posts.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "posts")
class Post(
    @Id
    val id: String? = null,
    val subject: String = "",
    val title: String = "",
    val url: String = "",
    val category: String = "",
    val writer: String = "",
    val date: String = "",
    var tags: List<String> = emptyList(),
    var shared: Boolean = false,
    val createdDt: String = "",
) {
    fun share() {
        shared = true
    }
}
