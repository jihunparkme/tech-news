package com.technews.aggregate.subscribe.dto

import com.technews.aggregate.subscribe.domain.Subscribe
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class SubscribeRequest(
    @field:NotBlank(message = "An email is required.")
    @field:Pattern(
        regexp = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}",
        message = "Email is not valid."
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
