package com.technews.scheduler;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Disabled
@ActiveProfiles("local")
@SpringBootTest
class SubscribeMailSchedulerTest {

    @Autowired
    private SubscribeMailScheduler subscribeMailScheduler;

    @Test
    void run() {
        subscribeMailScheduler.runScheduler();
    }
}