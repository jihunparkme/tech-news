package com.technews.aggregate.posts.spring.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SaveSpringPostRequestTest {

    @Test
    void when_isLatestDatePost_then_return_true() {
        final SaveSpringPostRequest release = SaveSpringPostRequest.builder()
                .date("April 30, 2024")
                .build();

        assertTrue(release.isLatestDatePost("April 29, 2024"));
        assertTrue(release.isLatestDatePost("April 26, 2024"));
    }

    @Test
    void when_isLatestDatePost_then_return_false() {
        final SaveSpringPostRequest release = SaveSpringPostRequest.builder()
                .date("April 30, 2024")
                .build();

        assertFalse(release.isLatestDatePost("April 30, 2024"));
        assertFalse(release.isLatestDatePost("April 31, 2024"));
    }
}