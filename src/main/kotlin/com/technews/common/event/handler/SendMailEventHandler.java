package com.technews.common.event.handler;

import com.technews.common.event.dto.SendMailEvent;
import com.technews.common.event.service.SendMailEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendMailEventHandler {
    private final SendMailEventService sendMailEventService;

    @Async
    @EventListener(SendMailEvent.class)
    public void handle(SendMailEvent event) {
        sendMailEventService.sendMail(event);
    }
}
