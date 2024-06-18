package com.technews.scheduler;

import org.junit.jupiter.api.Assertions;
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
    
    @Test 
    void getPostInfo() {
        final String postInfo = "Joakim Nordström on April 12, 2024";
        final String[] split = postInfo.split(" on ");

        Assertions.assertEquals("Joakim Nordström", split[0]);
        Assertions.assertEquals("April 12, 2024", split[1]);
    }

    @Test
    void getPostInfo_2() {
        final String postInfo = "April 12, 2024";
        final String[] split = postInfo.split(" on ");

        Assertions.assertEquals("April 12, 2024", split[0]);
    }
}