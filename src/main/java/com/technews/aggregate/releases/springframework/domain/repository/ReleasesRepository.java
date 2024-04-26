package com.technews.aggregate.releases.springframework.domain.repository;

import com.technews.aggregate.releases.springframework.domain.Release;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReleasesRepository extends MongoRepository<Release, String> {

    @Aggregation(pipeline = {
            "{ '$match': { 'project' : ?0 } }",
            "{ '$sort' : { 'version' : -1 } }",
            "{ '$limit' : 1 }"
    })
    List<Release> findByProjectOrderByVersionDescLimitOne(final String project);
}
