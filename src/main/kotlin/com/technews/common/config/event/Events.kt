package com.technews.common.config.event

import org.springframework.context.ApplicationEventPublisher

object Events {
    private lateinit var publisher: ApplicationEventPublisher

    fun setPublisher(publisher: ApplicationEventPublisher) {
        this.publisher = publisher
    }

    fun raise(event: Any) {
        publisher.publishEvent(event)
    }
}
