package com.technews.aggregate.subscribe.service

import com.technews.aggregate.subscribe.domain.Subscribe
import com.technews.aggregate.subscribe.domain.repository.SubscribeRepository
import com.technews.aggregate.subscribe.dto.SubscribeRequest
import com.technews.aggregate.subscribe.dto.SubscribeResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SubscribeService(
    private val subscribeRepository: SubscribeRepository,
) {
    @Transactional(readOnly = true)
    fun validSubscribe(request: SubscribeRequest): SubscribeResponse {
        val subscribe = subscribeRepository.findByEmail(request.email)
        return subscribe.let { SubscribeResponse.from(it) }
    }

    @Transactional
    fun subscribe(request: SubscribeRequest) =
        subscribeRepository.save(request.toSubscribe())

    @Transactional
    fun unsubscribe(request: SubscribeRequest) {
        val subscribe = subscribeRepository.findByEmail(request.email)
        subscribe.unsubscribe()
        subscribeRepository.save<Subscribe>(subscribe)
    }

    @Transactional(readOnly = true)
    fun findSubscriberEmail(): List<String> =
        subscribeRepository.findBySubscribeTrue().map { it.email }
}
