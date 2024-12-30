package com.technews.aggregate.releases.dto

import com.technews.aggregate.releases.createReleaseRequest
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe

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

    "when_isLatestDateVersion_then_return_true" {
        val release = SaveReleaseRequest(
            project = PROJECT,
            version = VERSION,
            date = "Apr 19, 2024",
            url = "url",
        )
        release.isLatestDateVersion("Apr 18, 2024").shouldBeTrue()
    }

    "when_isLatestDateVersion_then_return_false" {
        val release = SaveReleaseRequest(
            project = PROJECT,
            version = VERSION,
            date = "Apr 19, 2024",
            url = "url",
        )
        release.isLatestDateVersion("Apr 19, 2024").shouldBeFalse()
        release.isLatestDateVersion("Apr 20, 2024").shouldBeFalse()
    }

    "when_isLatestVersion_then_return_true" {
        val release = SaveReleaseRequest(
            project = PROJECT,
            version = "JDK 8u74",
            date = "Apr 19, 2024",
            url = "url",
        )

        "JDK 8u74".compareTo("JDK 8u92", ignoreCase = true) shouldBe -2
        "JDK 8u74".compareTo("JDK 8u74", ignoreCase = true) shouldBe 0
        "JDK 8u74".compareTo("JDK 8u71", ignoreCase = true) shouldBe 3
        "JDK 8u74".compareTo("", ignoreCase = true) shouldBe 8

        release.isLatestVersion("JDK 8u92").shouldBeFalse()
        release.isLatestVersion("JDK 8u91").shouldBeFalse()
        release.isLatestVersion("JDK 8u77").shouldBeFalse()
        release.isLatestVersion("JDK 8u74").shouldBeFalse()
        release.isLatestVersion("JDK 8u71").shouldBeTrue()
        release.isLatestVersion("").shouldBeTrue()
    }
}) {
    companion object {
        const val PROJECT: String = "spring-boot"
        const val VERSION: String = "Release v3.3.0-RC1"
    }
}
