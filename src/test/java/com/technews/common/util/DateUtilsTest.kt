package com.technews.common.util

import com.technews.common.util.DateUtils.parseEnglishDateFormat
import com.technews.common.util.DateUtils.parseGreenwichToSeoul
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class DateUtilsTest : StringSpec({
    "when_input_english_date_parse_then_true" {
        var result = parseEnglishDateFormat("Apr 18, 2024")
        result shouldBe "2024-04-18"

        result = parseEnglishDateFormat("Feb 22, 2024")
        result shouldBe "2024-02-22"

        result = parseEnglishDateFormat("May 1, 2024")
        result shouldBe "2024-05-01"

        result = parseEnglishDateFormat("April 30, 2024")
        result shouldBe "2024-04-30"

        result = parseEnglishDateFormat("March 05, 2024")
        result shouldBe "2024-03-05"

        result = parseEnglishDateFormat("April 9, 2024")
        result shouldBe "2024-04-09"

        result = parseEnglishDateFormat("6 November 2024")
        result shouldBe "2024-11-06"

        result = parseEnglishDateFormat("21 July 2023")
        result shouldBe "2023-07-21"

        result = parseEnglishDateFormat("10 Oct 2023")
        result shouldBe "2023-10-10"
    }

    "when_input_greenwich_date_parse_then_true" {
        var result = parseGreenwichToSeoul("Tue, 10 Dec 2024 17:55:00 GMT")
        result shouldBe "2024-12-11"

        result = parseGreenwichToSeoul("Tue, 15 Oct 2024 19:42:00 GMT")
        result shouldBe "2024-10-16"

        result = parseGreenwichToSeoul("Tue, 7 Nov 2023 18:30:00 GMT")
        result shouldBe "2023-11-08"
    }
})
