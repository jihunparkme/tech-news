package com.technews.common.util

import com.technews.common.util.DateUtils.getFormattedDate
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions

class DateUtilsTest : StringSpec({
    "when_formatted_date_then_true" {
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
})
