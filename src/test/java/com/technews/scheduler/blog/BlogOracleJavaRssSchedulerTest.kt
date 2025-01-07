package com.technews.scheduler.blog

import com.technews.IntegrationTest
import io.kotest.core.spec.style.StringSpec
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class BlogOracleJavaRssSchedulerTest(
    @Autowired private val blogOracleJavaRssScheduler: BlogOracleJavaRssScheduler,
) : StringSpec({
        "scrape a oracle java blog rss" {
            blogOracleJavaRssScheduler.runSchedule()
        }
    })
