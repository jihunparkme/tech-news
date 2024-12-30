package com.technews.common.event

import com.technews.aggregate.posts.domain.Post
import com.technews.aggregate.releases.domain.Release
import com.technews.aggregate.subscribe.dto.SubscriberMailContents

fun createSubscriberMailContents(): SubscriberMailContents {
    return SubscriberMailContents(
        springReleases = listOf(
            Release(
                url = "www.google.com",
                project = "spring-framework",
                version = "Release v6.2.1",
            ),
            Release(
                url = "www.google.com",
                project = "spring-boot",
                version = "Release v3.4.1",
            ),
        ),
        springPosts = listOf(
            Post(
                url = "www.google.com",
                title = "AAA",
            ),
            Post(
                url = "www.google.com",
                title = "BBB",
            ),
        ),
        javaReleases = listOf(
            Release(
                url = "www.google.com",
                project = "JDK 21",
                version = "JDK 21.0.5",
            ),
            Release(
                url = "www.google.com",
                project = "JDK 18",
                version = "JDK 18.0.4",
            ),
        ),
        javaPosts = listOf(
            Post(
                url = "www.google.com",
                title = "CCC",
            ),
            Post(
                url = "www.google.com",
                title = "DDD",
            ),
        ),
    )
}
