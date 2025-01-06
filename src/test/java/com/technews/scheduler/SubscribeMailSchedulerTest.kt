package com.technews.scheduler

import com.technews.IntegrationTest
import com.technews.aggregate.subscribe.dto.SubscribeRequest
import com.technews.aggregate.subscribe.service.SubscribeService
import io.kotest.core.spec.style.StringSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@Transactional
@IntegrationTest
class SubscribeMailSchedulerTest(
    @Autowired private val subscribeMailScheduler: SubscribeMailScheduler,
    @Autowired private val subscribeService: SubscribeService,
) : StringSpec({
        "subscribe mail".config(enabled = false) {
            val request = SubscribeRequest("jihunpark.tech@gmail.com")
            subscribeService.subscribe(request)
            subscribeMailScheduler.runSchedule()
        }
    })
