package com.technews.aggregate.releases.springframework.domain.repository;

import com.technews.aggregate.releases.domain.Release;
import com.technews.aggregate.releases.domain.repository.ReleasesRepository;
import com.technews.aggregate.releases.dto.ReleaseResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    void findByProjectOrderAllByVersionDescLimitOneIn() {
        final List<Release> releases = releasesRepository.findByProjectOrderByVersionDescLimitOne("spring-framework");

        final List<ReleaseResponse> result = releases.stream().
                map(release -> ReleaseResponse.of(release))
                .collect(Collectors.toList());

        result.forEach(System.out::println);
    }

    @Test
    void find_all_pageable() {
        final int page = 0;
        final int size = 10;
        final PageRequest pageable = PageRequest.of(
                page, size,
                Sort.by("createdDt").descending().and(Sort.by("version").descending()));

        final Page<Release> releases = releasesRepository.findAll(pageable);

        final List<ReleaseResponse> result = releases.getContent().stream().
                map(release -> ReleaseResponse.of(release))
                .collect(Collectors.toList());

        result.forEach(System.out::println);
    }

    @Test
    void find_by_category_pageable() {
        final int page = 0;
        final int size = 10;
        final List<String> categories = List.of("spring-batch", "spring-boot");
        final PageRequest pageable = PageRequest.of(
                page, size,
                Sort.by("createdDt").descending().and(Sort.by("version").descending()));

        final Page<Release> releases = releasesRepository.findByProjectIn(categories, pageable);

        final List<ReleaseResponse> result = releases.getContent().stream().
                map(release -> ReleaseResponse.of(release))
                .collect(Collectors.toList());

        result.forEach(System.out::println);
    }
}