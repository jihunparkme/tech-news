package com.technews.scheduler

import com.technews.IntegrationTest
import io.kotest.core.spec.style.StringSpec
import org.junit.jupiter.api.Disabled
import org.springframework.beans.factory.annotation.Autowired

@Disabled
@IntegrationTest
class BlogOracleJavaSchedulerTest(
    @Autowired private val blogOracleJavaScheduler: BlogOracleJavaScheduler
) : StringSpec({
    "scrape a oracle java blog post" {
        blogOracleJavaScheduler.runScheduled()
    }
})
