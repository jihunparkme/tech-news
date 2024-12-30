package com.technews.aggregate.subscribe.dto

import com.technews.aggregate.posts.domain.Post
import com.technews.aggregate.releases.domain.Release
import com.technews.aggregate.subscribe.domain.Subscribe
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class SubscribeRequest(
    @field:NotBlank(message = "An email is required.")
    @field:Pattern(
        regexp = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}",
        message = "Email is not valid.",
    )
    @field:Size(min = 3, max = 50, message = "The email length must be between 3 and 50 characters.")
    val email: String,
) {
    fun toSubscribe(): Subscribe =
        Subscribe(
            email = email,
            subscribe = true,
            startDt = LocalDateTime.now(),
            endDt = LocalDateTime.now(),
        )

    fun toUnsubscribe(): Subscribe =
        Subscribe(
            subscribe = false,
            endDt = LocalDateTime.now(),
        )
}

data class SubscribeResponse(
    val email: String = "",
    val subscribe: Boolean = false,
    val startDt: String = "",
    val endDt: String = "",
    val empty: Boolean = true,
) {
    companion object {
        fun from(subscribe: Subscribe): SubscribeResponse =
            SubscribeResponse(
                email = subscribe.email,
                subscribe = subscribe.subscribe,
                startDt = subscribe.startDt.toString(),
                endDt = subscribe.endDt.toString(),
                empty = false,
            )
    }
}

data class SubscriberMailContents(
    val springReleases: List<Release> = emptyList(),
    val javaReleases: List<Release> = emptyList(),
    val springPosts: List<Post> = emptyList(),
    val javaPosts: List<Post> = emptyList(),
)
