package com.technews.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("local")
@SpringBootTest
class JavaInsideBlogSchedulerTest {

    @Autowired
    private JavaInsideBlogScheduler javaInsideBlogScheduler;

    @Test
    void run() {
        javaInsideBlogScheduler.runScheduled();
    }
}