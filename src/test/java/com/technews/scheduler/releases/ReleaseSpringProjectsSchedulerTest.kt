package com.technews.scheduler.releases

import com.technews.IntegrationTest
import io.kotest.core.spec.style.StringSpec
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class ReleaseSpringProjectsSchedulerTest(
    @Autowired private val releaseSpringProjectsScheduler: ReleaseSpringProjectsScheduler,
) : StringSpec({
        "scrape a spring project releases post".config(enabled = false) {
            releaseSpringProjectsScheduler.runSchedule()
        }
    })
