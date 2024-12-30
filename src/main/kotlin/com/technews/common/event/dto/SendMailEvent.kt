package com.technews.common.event.dto

import com.technews.aggregate.subscribe.dto.SubscriberMailContents

class SendMailEvent(
    val subject: String,
    val contents: SubscriberMailContents,
    val addressList: List<String>,
)
