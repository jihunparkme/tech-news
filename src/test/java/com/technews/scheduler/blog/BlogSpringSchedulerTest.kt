package com.technews.scheduler.blog

import com.technews.IntegrationTest
import io.kotest.core.spec.style.StringSpec
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class BlogSpringSchedulerTest(
    @Autowired private val blogSpringScheduler: BlogSpringScheduler,
) : StringSpec({
        "scrape a spring blog post".config(enabled = false) {
            blogSpringScheduler.runSchedule()
        }
    })
