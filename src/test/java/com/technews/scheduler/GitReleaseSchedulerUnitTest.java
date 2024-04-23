package com.technews.scheduler;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GitReleaseSchedulerUnitTest {

    @Test
    void convertDate() {
        final String result = GitReleaseScheduler.convertDate("Apr 18, 2024");
        assertEquals("2024-04-18", result);
    }
}