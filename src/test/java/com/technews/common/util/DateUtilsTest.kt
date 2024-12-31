package com.technews.common.util

import com.technews.common.util.DateUtils.getFormattedDate
import com.technews.common.util.DateUtils.gmtToSeoul
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions

class DateUtilsTest : StringSpec({
    "when_input_english_date_parse_then_true" {
        var result = getFormattedDate("Apr 18, 2024")
        result shouldBe "2024-04-18"

        result = getFormattedDate("Feb 22, 2024")
        result shouldBe "2024-02-22"

        result = getFormattedDate("May 1, 2024")
        result shouldBe "2024-05-01"

        result = getFormattedDate("April 30, 2024")
        result shouldBe "2024-04-30"

        result = getFormattedDate("March 05, 2024")
        result shouldBe "2024-03-05"

        result = getFormattedDate("April 9, 2024")
        Assertions.assertEquals("2024-04-09", result)
        result shouldBe "2024-04-09"
    }

    "when_input_greenwich_date_parse_then_true" {
        var result = gmtToSeoul("Tue, 10 Dec 2024 17:55:00 GMT")
        result shouldBe "2024-12-11"

        result = gmtToSeoul("Tue, 15 Oct 2024 19:42:00 GMT")
        result shouldBe "2024-10-16"

        result = gmtToSeoul("Tue, 7 Nov 2023 18:30:00 GMT")
        result shouldBe "2024-11-08"
    }
})
