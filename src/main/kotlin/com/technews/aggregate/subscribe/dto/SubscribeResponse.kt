package com.technews.aggregate.subscribe.dto

import com.technews.aggregate.subscribe.domain.Subscribe

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
