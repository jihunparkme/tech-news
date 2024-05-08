package com.technews.scheduler;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Disabled
@ActiveProfiles("local")
@SpringBootTest
class BlogJavaInsideSchedulerTest {

    @Autowired
    private BlogJavaInsideScheduler blogJavaInsideScheduler;

    @Test
    void run() {
        blogJavaInsideScheduler.runScheduled();
    }
}