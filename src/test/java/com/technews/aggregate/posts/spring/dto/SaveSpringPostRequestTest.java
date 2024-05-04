package com.technews.aggregate.posts.spring.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SaveSpringPostRequestTest {

    @Test
    void when_isLatestDatePost_then_return_true() {
        final SaveSpringPostRequest release = SaveSpringPostRequest.builder()
                .date("2024-04-30")
                .build();

        assertTrue(release.isLatestDatePost("2024-04-29"));
        assertTrue(release.isLatestDatePost("2024-04-26"));
    }

    @Test
    void when_isLatestDatePost_then_return_false() {
        final SaveSpringPostRequest release = SaveSpringPostRequest.builder()
                .date("2024-04-30")
                .build();

        assertFalse(release.isLatestDatePost("2024-04-30"));
        assertFalse(release.isLatestDatePost("2024-04-31"));
    }
}