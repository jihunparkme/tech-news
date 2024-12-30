package com.technews.scheduler

import com.technews.IntegrationTest
import io.kotest.core.spec.style.StringSpec
import org.junit.jupiter.api.Disabled
import org.springframework.beans.factory.annotation.Autowired

@Disabled
@IntegrationTest
class BlogSpringSchedulerTest(
    @Autowired private val blogSpringScheduler: BlogSpringScheduler,
) : StringSpec({
        "scrape a spring blog post" {
            blogSpringScheduler.runScheduler()
        }
    })
