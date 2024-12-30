package com.technews.aggregate.subscribe.controller

import com.technews.aggregate.subscribe.dto.SubscribeRequest
import com.technews.aggregate.subscribe.service.SubscribeService
import com.technews.common.constant.Result.SUCCESS
import com.technews.common.dto.BasicResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/subscribe")
class SubscribeController(
    private val subscribeService: SubscribeService,
) {
    @PostMapping("/valid")
    fun validSubscribe(@RequestBody @Valid request: SubscribeRequest): ResponseEntity<*> =
        BasicResponse.ok(subscribeService.validSubscribe(request))

    @PostMapping
    fun subscribe(@RequestBody @Valid request: SubscribeRequest): ResponseEntity<*> {
        subscribeService.subscribe(request)
        return BasicResponse.ok(SUCCESS)
    }

    @RequestMapping("/unsubscribe")
    fun unsubscribe(@RequestBody @Valid request: SubscribeRequest): ResponseEntity<*> {
        subscribeService.unsubscribe(request)
        return BasicResponse.ok(SUCCESS)
    }
}
