package com.technews.scheduler;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Disabled
@ActiveProfiles("local")
@SpringBootTest
public class AllSchedulerTest {

    @Autowired
    private ReleaseSpringProjectsScheduler releaseSpringProjectsScheduler;
    @Autowired
    private ReleasesJavaScheduler releasesJavaScheduler;
    @Autowired
    private BlogSpringScheduler blogSpringScheduler;
    @Autowired
    private BlogJavaInsideScheduler blogJavaInsideScheduler;
    @Autowired
    private SubscribeMailScheduler subscribeMailScheduler;

    @Test
    void run() {
        releaseSpringProjectsScheduler.runScheduler();
        releasesJavaScheduler.runScheduler();
        blogSpringScheduler.runScheduler();
        blogJavaInsideScheduler.runScheduled();
        subscribeMailScheduler.runScheduler();
    }
}
