package com.technews.aggregate.releases.dto

import com.technews.aggregate.releases.createReleaseRequest
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

class SaveReleaseRequestTest : StringSpec({
    "마지막 일자의 릴리즈 버전인지 확인" {
        val request = createReleaseRequest(date = "Dec 25, 2024")

        assertSoftly(request) {
            request.isLatestDateVersion("").shouldBeTrue()
            request.isLatestDateVersion("Dec 24, 2024").shouldBeTrue()
            request.isLatestDateVersion("Dec 25, 2024").shouldBeFalse()
            request.isLatestDateVersion("2024-12-25").shouldBeFalse()
        }
    }

    "마지막 릴리즈 버전인지 확인" {
        val request = createReleaseRequest(version = "6.2.1")

        assertSoftly(request) {
            request.isLatestVersion("").shouldBeTrue()
            request.isLatestVersion("6.2.0").shouldBeTrue()
            request.isLatestVersion("6.1.9").shouldBeTrue()
        }
    }
})
