package com.technews.scheduler.blog

import com.technews.IntegrationTest
import io.kotest.core.spec.style.StringSpec
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class BlogOracleJavaSchedulerTest(
    @Autowired private val blogOracleJavaScheduler: BlogOracleJavaScheduler,
) : StringSpec({
        "scrape a oracle java blog post".config(enabled = false) {
            blogOracleJavaScheduler.runScheduled()
        }
    })
