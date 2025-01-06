package com.technews.scheduler.releases

import com.technews.IntegrationTest
import io.kotest.core.spec.style.StringSpec

@IntegrationTest
class ReleasesKafkaSchedulerTest(
    private val releasesKafkaScheduler: ReleasesKafkaScheduler,
) : StringSpec({
        "scrap a kafka release post".config(enabled = false) {
            releasesKafkaScheduler.runSchedule()
        }
    })
