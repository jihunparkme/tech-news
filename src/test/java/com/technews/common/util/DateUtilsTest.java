package com.technews.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateUtilsTest {

    @Test
    void when_convert_english_format1_then_true() {
        String result = DateUtils.getFormattedDate("Apr 18, 2024", DateUtils.ENGLISH_FORMATTER_1);
        assertEquals("2024-04-18", result);

        result = DateUtils.getFormattedDate("Feb 22, 2024", DateUtils.ENGLISH_FORMATTER_1);
        assertEquals("2024-02-22", result);
    }

    @Test
    void when_convert_english_format2_then_true() {
        String result = DateUtils.getFormattedDate("April 30, 2024", DateUtils.ENGLISH_FORMATTER_2);
        assertEquals("2024-04-30", result);

        result = DateUtils.getFormattedDate("March 05, 2024", DateUtils.ENGLISH_FORMATTER_2);
        assertEquals("2024-03-05", result);
    }
}