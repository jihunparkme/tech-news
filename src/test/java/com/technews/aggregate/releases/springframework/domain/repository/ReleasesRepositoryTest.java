package com.technews.aggregate.releases.springframework.domain.repository;

import com.technews.aggregate.releases.springframework.domain.Release;
import com.technews.aggregate.releases.springframework.dto.ReleaseResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;

@ActiveProfiles("local")
@DataMongoTest
class ReleasesRepositoryTest {

    @Autowired
    private ReleasesRepository releasesRepository;

    @Test
    void findAll() {
        final List<Release> result = releasesRepository.findAll();

        System.out.println("result.size() = " + result.size());
    }

    @Test
    void test() {
        final List<Release> releases = releasesRepository.findByProjectOrderByVersionDescLimitOne("spring-framework");

        final List<ReleaseResponse> result = releases.stream().
                map(release -> ReleaseResponse.of(release))
                .collect(Collectors.toList());

        result.forEach(System.out::println);
    }
}