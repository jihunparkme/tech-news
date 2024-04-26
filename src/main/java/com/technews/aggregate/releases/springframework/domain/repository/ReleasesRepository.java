package com.technews.aggregate.releases.springframework.domain.repository;

import com.technews.aggregate.releases.springframework.domain.Release;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReleasesRepository extends MongoRepository<Release, String> {
}
