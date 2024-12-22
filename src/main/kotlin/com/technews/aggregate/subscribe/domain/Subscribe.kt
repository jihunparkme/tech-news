package com.technews.aggregate.subscribe.domain

import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "subscribe")
class Subscribe(
    val id: String = "",
    val email: String = "",
    var subscribe: Boolean = false,
    val startDt: LocalDateTime = LocalDateTime.now(),
    var endDt: LocalDateTime = LocalDateTime.now(),
) {
    fun unsubscribe() {
        subscribe = false
        endDt = LocalDateTime.now()
    }
}