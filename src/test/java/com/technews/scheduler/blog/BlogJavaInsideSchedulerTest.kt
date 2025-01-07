package com.technews.scheduler.blog

import com.technews.IntegrationTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class BlogJavaInsideSchedulerTest(
    @Autowired private val blogJavaInsideScheduler: BlogJavaInsideScheduler,
) : StringSpec({
        "scrape java inside post" {
            blogJavaInsideScheduler.runSchedule()
        }

        "parse the post writer and date" {
            val postInfo = "Joakim Nordström on April 12, 2024"
            val split = postInfo.split(" on ")

            split[0] shouldBe "Joakim Nordström"
            split[1] shouldBe "April 12, 2024"
        }

        "parse the post date" {
            val postInfo = "April 12, 2024"
            val split = postInfo.split(" on ")

            split[0] shouldBe "April 12, 2024"
        }
    })
