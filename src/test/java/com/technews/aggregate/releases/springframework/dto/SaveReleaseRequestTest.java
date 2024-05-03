package com.technews.aggregate.releases.springframework.dto;

import com.technews.aggregate.releases.dto.SaveReleaseRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SaveReleaseRequestTest {
    public static final String PROJECT = "spring-boot";
    public static final String VERSION = "Release v3.3.0-RC1";

    @Test
    void when_isLatestDateVersion_then_return_true() {
        final SaveReleaseRequest release = SaveReleaseRequest.builder()
                .project(PROJECT)
                .version(VERSION)
                .date("Apr 19, 2024")
                .url("url")
                .build();

        assertTrue(release.isLatestDateVersion("Apr 18, 2024"));
    }

    @Test
    void when_isLatestDateVersion_then_return_false() {
        final SaveReleaseRequest release = SaveReleaseRequest.builder()
                .project(PROJECT)
                .version(VERSION)
                .date("Apr 19, 2024")
                .url("url")
                .build();

        assertFalse(release.isLatestDateVersion("Apr 19, 2024"));
        assertFalse(release.isLatestDateVersion("Apr 20, 2024"));
    }

    @Test 
    void when_isLatestVersion_then_return_true() {
        final SaveReleaseRequest release = SaveReleaseRequest.builder()
                .project(PROJECT)
                .version("JDK 8u74")
                .date("Apr 19, 2024")
                .url("url")
                .build();

        assertTrue(release.isLatestVersion("JDK 8u92"));
        assertTrue(release.isLatestVersion("JDK 8u91"));
        assertTrue(release.isLatestVersion("JDK 8u77"));
        assertFalse(release.isLatestVersion("JDK 8u74"));
        assertFalse(release.isLatestVersion("JDK 8u71"));
        assertFalse(release.isLatestVersion(""));
    }
}