package com.technews.aggregate.releases.domain.repository;

import com.technews.aggregate.releases.domain.Release;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<Release> findAll(Pageable pageable);

    Page<Release> findByProjectIn(List<String> project, Pageable pageable);

    List<Release> findBySharedFalse();
}
