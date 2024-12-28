package com.technews.scheduler

import com.technews.IntegrationTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class BlogJavaInsideSchedulerTest(
    @Autowired private val blogJavaInsideScheduler: BlogJavaInsideScheduler
) : StringSpec({
    "Inside Java 게시물 스크래핑 동작" {
        blogJavaInsideScheduler.runScheduled()
    }

    "게시물 작성자와 날짜 파싱" {
        val postInfo = "Joakim Nordström on April 12, 2024"
        val split = postInfo.split(" on ")

        split[0] shouldBe "Joakim Nordström"
        split[1] shouldBe "April 12, 2024"
    }

    "게시물 날짜 파싱" {
        val postInfo = "April 12, 2024"
        val split = postInfo.split(" on ")

        split[0] shouldBe "April 12, 2024"
    }
})
