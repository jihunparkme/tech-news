package com.technews.scheduler

import com.technews.IntegrationTest
import com.technews.scheduler.blog.BlogJavaInsideScheduler
import com.technews.scheduler.blog.BlogOracleJavaRssScheduler
import com.technews.scheduler.blog.BlogSpringScheduler
import com.technews.scheduler.releases.ReleaseSpringProjectsScheduler
import com.technews.scheduler.releases.ReleasesJavaScheduler
import io.kotest.core.spec.style.StringSpec
import org.junit.jupiter.api.Disabled
import org.springframework.beans.factory.annotation.Autowired

@Disabled
@IntegrationTest
class AllSchedulerTest(
    @Autowired private val releaseSpringProjectsScheduler: ReleaseSpringProjectsScheduler,
    @Autowired private val releasesJavaScheduler: ReleasesJavaScheduler,
    @Autowired private val blogSpringScheduler: BlogSpringScheduler,
    @Autowired private val blogJavaInsideScheduler: BlogJavaInsideScheduler,
    @Autowired private val subscribeMailScheduler: SubscribeMailScheduler,
    @Autowired private val javaBlogOracleJavaRssScheduler: BlogOracleJavaRssScheduler,
) : StringSpec({
        "scrape all post" {
            releaseSpringProjectsScheduler.runSchedule()
            releasesJavaScheduler.runSchedule()
            blogSpringScheduler.runSchedule()
            blogJavaInsideScheduler.runSchedule()
            subscribeMailScheduler.runSchedule()
            javaBlogOracleJavaRssScheduler.runSchedule()
        }
    })
