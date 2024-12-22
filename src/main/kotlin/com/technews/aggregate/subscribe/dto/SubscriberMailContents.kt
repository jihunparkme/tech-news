package com.technews.aggregate.subscribe.dto

import com.technews.aggregate.posts.domain.Post
import com.technews.aggregate.releases.domain.Release

data class SubscriberMailContents(
    val springReleases: List<Release> = emptyList(),
    val javaReleases: List<Release> = emptyList(),
    val springPosts: List<Post> = emptyList(),
    val javaPosts: List<Post> = emptyList(),
)