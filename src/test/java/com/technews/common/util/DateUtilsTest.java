package com.technews.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateUtilsTest {

    @Test
    void when_formatted_date_then_true() {
        String result = DateUtils.getFormattedDate("Apr 18, 2024");
        assertEquals("2024-04-18", result);

        result = DateUtils.getFormattedDate("Feb 22, 2024");
        assertEquals("2024-02-22", result);

        result = DateUtils.getFormattedDate("May 1, 2024");
        assertEquals("2024-05-01", result);

        result = DateUtils.getFormattedDate("April 30, 2024");
        assertEquals("2024-04-30", result);

        result = DateUtils.getFormattedDate("March 05, 2024");
        assertEquals("2024-03-05", result);

        result = DateUtils.getFormattedDate("April 9, 2024");
        assertEquals("2024-04-09", result);
    }
}