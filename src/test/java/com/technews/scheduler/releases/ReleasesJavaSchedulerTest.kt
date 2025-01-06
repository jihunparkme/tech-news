package com.technews.scheduler.releases

import com.technews.IntegrationTest
import io.kotest.core.spec.style.StringSpec
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class ReleasesJavaSchedulerTest(
    @Autowired private val releasesJavaScheduler: ReleasesJavaScheduler,
) : StringSpec({
        "scrape a java release post".config(enabled = false) {
            releasesJavaScheduler.runSchedule()
        }
    })
