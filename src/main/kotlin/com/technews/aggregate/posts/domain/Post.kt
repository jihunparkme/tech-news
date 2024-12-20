package com.technews.aggregate.posts.domain

import org.apache.commons.lang3.StringUtils
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "posts")
data class Post(
    val id: String = StringUtils.EMPTY,
    val subject: String = StringUtils.EMPTY,
    val title: String = StringUtils.EMPTY,
    val url: String = StringUtils.EMPTY,
    val category: String = StringUtils.EMPTY,
    val writer: String = StringUtils.EMPTY,
    val date: String = StringUtils.EMPTY,
    var tags: List<String> = emptyList(),
    var shared: Boolean = false,
    val createdDt: String = StringUtils.EMPTY,
) {
    fun share() {
        this.shared = true
    }
}
