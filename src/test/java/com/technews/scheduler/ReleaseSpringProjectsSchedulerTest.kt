package com.technews.scheduler

import com.technews.IntegrationTest
import io.kotest.core.spec.style.StringSpec
import org.junit.jupiter.api.Disabled
import org.springframework.beans.factory.annotation.Autowired

@Disabled
@IntegrationTest
class ReleaseSpringProjectsSchedulerTest(
    @Autowired private val releaseSpringProjectsScheduler: ReleaseSpringProjectsScheduler,
) : StringSpec({
        "scrape a spring project releases post" {
            releaseSpringProjectsScheduler.runSchedule()
        }
    })
