package com.technews.scheduler.releases

import com.technews.IntegrationTest
import io.kotest.core.spec.style.StringSpec
import org.junit.jupiter.api.Disabled

@Disabled
@IntegrationTest
class ReleasesKafkaSchedulerTest(
    private val releasesKafkaScheduler: ReleasesKafkaScheduler,
) : StringSpec({
    "scrap a kafka release post" {
        releasesKafkaScheduler.runSchedule()
    }
})
