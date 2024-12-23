package com.technews.common.event.dto

class SendMailEvent (
    val subject: String,
    val contents: String,
    val addressList: List<String>,
)
