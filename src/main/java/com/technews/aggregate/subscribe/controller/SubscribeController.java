package com.technews.aggregate.subscribe.controller;

import com.technews.aggregate.subscribe.dto.SubscribeRequest;
import com.technews.aggregate.subscribe.dto.SubscribeResponse;
import com.technews.aggregate.subscribe.service.SubscribeService;
import com.technews.common.dto.BasicResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.technews.common.constant.Result.SUCCESS;

@Slf4j
@RestController
@RequestMapping("/subscribe")
@RequiredArgsConstructor
public class SubscribeController {

    private final SubscribeService subscribeService;

    @PostMapping("/valid")
    public ResponseEntity validSubscribe(@Valid @RequestBody SubscribeRequest request) {
        final SubscribeResponse response = subscribeService.validSubscribe(request);
        return BasicResponse.ok(response);
    }

    @PostMapping
    public ResponseEntity subscribe(@Valid @RequestBody SubscribeRequest request) {
        subscribeService.subscribe(request);
        return BasicResponse.ok(SUCCESS);
    }

    @RequestMapping("/unsubscribe")
    public ResponseEntity unsubscribe(@Valid @RequestBody SubscribeRequest request) {
        subscribeService.unsubscribe(request);
        return BasicResponse.ok(SUCCESS);
    }
}
