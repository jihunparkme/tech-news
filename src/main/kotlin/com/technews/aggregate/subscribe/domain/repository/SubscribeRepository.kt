package com.technews.aggregate.subscribe.domain.repository

import com.technews.aggregate.subscribe.domain.Subscribe
import org.springframework.data.mongodb.repository.MongoRepository

interface SubscribeRepository : MongoRepository<Subscribe, String> {
    fun findByEmail(email: String): Subscribe?

    fun findBySubscribeTrue(): List<Subscribe>
}
