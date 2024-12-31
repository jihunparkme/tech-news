package com.technews.scheduler

import com.technews.IntegrationTest
import io.kotest.core.spec.style.StringSpec
import org.junit.jupiter.api.Disabled
import org.springframework.beans.factory.annotation.Autowired

@Disabled
@IntegrationTest
class BlogOracleJavaRssSchedulerTest(
    @Autowired private val blogOracleJavaRssScheduler: BlogOracleJavaRssScheduler,
) : StringSpec({
        "scrape a oracle java blog rss" {
            blogOracleJavaRssScheduler.runSchedule()
        }
    })
