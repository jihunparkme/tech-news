package com.technews.aggregate.posts

import com.technews.aggregate.posts.domain.Post

fun createPost(
    id: String = "",
    subject: String = "",
    title: String = "",
    url: String = "",
    category: String = "",
    writer: String = "",
    date: String = "",
    tags: List<String> = emptyList(),
    shared: Boolean = false,
    createdDt: String = "",
): Post {
    return Post(id, subject, title, url, category, writer, date, tags, shared, createdDt)
}
