package com.technews.aggregate.subscribe.service;

import com.technews.aggregate.subscribe.domain.Subscribe;
import com.technews.aggregate.subscribe.domain.repository.SubscribeRepository;
import com.technews.aggregate.subscribe.dto.SubscribeRequest;
import com.technews.aggregate.subscribe.dto.SubscribeResponse;
import com.technews.common.exception.NotFoundSubscribe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;

    @Transactional(readOnly = true)
    public SubscribeResponse validSubscribe(SubscribeRequest request) {
        final Optional<Subscribe> subscribeOpt = subscribeRepository.findByEmail(request.getEmail());
        if (subscribeOpt.isEmpty()) {
            return SubscribeResponse.EMPTY;
        }

        return SubscribeResponse.of(subscribeOpt.get());
    }

    @Transactional
    public void subscribe(SubscribeRequest request) {
        Subscribe subscribe = request.toSubscribe();
        subscribeRepository.save(subscribe);
    }

    @Transactional
    public void unsubscribe(SubscribeRequest request) {
        final Optional<Subscribe> subscribeOpt = subscribeRepository.findByEmail(request.getEmail());
        if (subscribeOpt.isEmpty()) {
            throw new NotFoundSubscribe();
        }

        final Subscribe subscribe = subscribeOpt.get();
        subscribe.unsubscribe();
        subscribeRepository.save(subscribe);
    }

    @Transactional(readOnly = true)
    public List<SubscribeResponse> findSubscriber() {
        final Optional<List<Subscribe>> subscribersOpt = subscribeRepository.findBySubscribeTrue();
        if (subscribersOpt.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        return subscribersOpt.get().stream()
                .map(subscribe -> SubscribeResponse.of(subscribe))
                .collect(Collectors.toList());
    }
}
