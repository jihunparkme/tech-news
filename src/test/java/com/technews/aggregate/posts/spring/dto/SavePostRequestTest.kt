package com.technews.aggregate.posts.spring.dto

import com.technews.aggregate.posts.dto.SavePostRequest
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

class SavePostRequestTest : StringSpec({
    "latest post should be return true" {
        val release = SavePostRequest(
            date = "2024-04-30",
        )
        assertSoftly(release) {
            release.isLatestDatePost("2024-04-29").shouldBeTrue()
            release.isLatestDatePost("2024-04-26").shouldBeTrue()
        }
    }

    "not latest post should be return false" {
        val release = SavePostRequest(
            date = "2024-04-30",
        )
        assertSoftly(release) {
            release.isLatestDatePost("2024-04-30").shouldBeFalse()
            release.isLatestDatePost("2024-04-31").shouldBeFalse()
        }
    }
})
