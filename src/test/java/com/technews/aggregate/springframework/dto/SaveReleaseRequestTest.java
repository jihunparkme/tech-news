package com.technews.aggregate.springframework.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SaveReleaseRequestTest {
    public static final String PROJECT = "spring-boot";
    public static final String VERSION = "Release v3.3.0-RC1";

    @Test
    void when_isNewVersion_then_return_true() {
        final SaveReleaseRequest release = SaveReleaseRequest.builder()
                .project(PROJECT)
                .version(VERSION)
                .date("Apr 19, 2024")
                .url("url")
                .build();

        assertTrue(release.isNewVersion("Apr 18, 2024"));
    }

    @Test
    void when_isNewVersion_then_return_false() {
        final SaveReleaseRequest release = SaveReleaseRequest.builder()
                .project(PROJECT)
                .version(VERSION)
                .date("Apr 19, 2024")
                .url("url")
                .build();

        assertFalse(release.isNewVersion("Apr 19, 2024"));
        assertFalse(release.isNewVersion("Apr 20, 2024"));
    }
}