package com.technews.common.event.service;

import com.technews.common.config.event.Events;
import com.technews.common.event.dto.SendMailEvent;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Optional;

@Disabled
@ActiveProfiles("local")
@SpringBootTest
class SendMailEventServiceTest {
    @Test
    void sendAuthCodeMailEvent() throws Exception {
        String subject = "TEST: Java & Spring News";
        Events.raise(SendMailEvent.builder()
                .subject(subject)
                .contents("test content")
                .addressList(Optional.of(Arrays.asList("jihunpark.tech@gmail.com")))
                .build());
    }
}