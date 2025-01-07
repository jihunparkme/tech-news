package com.technews.scheduler.blog

import com.technews.IntegrationTest
import io.kotest.core.spec.style.StringSpec
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class BlogKafkaSchedulerTest(
    @Autowired private val blogKafkaScheduler: BlogKafkaScheduler,
): StringSpec({
    "scrape a kafka blog post".config(enabled = false) {
        blogKafkaScheduler.runSchedule()
    }
})
