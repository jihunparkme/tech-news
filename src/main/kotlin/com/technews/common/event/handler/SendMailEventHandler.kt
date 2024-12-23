package com.technews.common.event.handler

import com.technews.common.event.dto.SendMailEvent
import com.technews.common.event.service.SendMailEventService
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class SendMailEventHandler(
    private val sendMailEventService: SendMailEventService,
) {
    @Async
    @EventListener(SendMailEvent::class)
    fun handle(event: SendMailEvent) {
        sendMailEventService.sendMail(event)
    }
}
