package com.technews.aggregate.subscribe.domain.repository;

import com.technews.aggregate.subscribe.domain.Subscribe;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SubscribeRepository extends MongoRepository<Subscribe, String> {
    Optional<Subscribe> findByEmail(String email);
}
