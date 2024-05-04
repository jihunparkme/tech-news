package com.technews.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("local")
@SpringBootTest
class SpringBlogSchedulerTest {

    @Autowired
    private SpringBlogScheduler springBlogScheduler;

    @Test
    void run() {
        springBlogScheduler.runScheduler();
    }
}