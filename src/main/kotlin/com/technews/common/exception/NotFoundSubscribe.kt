package com.technews.common.exception

class NotFoundSubscribe(
    message: String = "Not Found Subscribe",
    cause: Throwable
) : RuntimeException(message, cause)