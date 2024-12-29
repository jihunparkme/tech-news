package com.technews.scheduler

import com.technews.IntegrationTest
import io.kotest.core.spec.style.StringSpec
import org.junit.jupiter.api.Disabled
import org.springframework.beans.factory.annotation.Autowired

@Disabled
@IntegrationTest
class ReleasesJavaSchedulerTest(
    @Autowired private val releasesJavaScheduler: ReleasesJavaScheduler
) : StringSpec({
    "scrape a java release post" {
        releasesJavaScheduler.runScheduler()
    }
})
