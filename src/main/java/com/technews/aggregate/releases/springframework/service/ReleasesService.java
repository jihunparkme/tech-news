package com.technews.aggregate.releases.springframework.service;

import com.technews.aggregate.releases.springframework.domain.Release;
import com.technews.aggregate.releases.springframework.domain.repository.ReleasesFireStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReleasesService {

    private final ReleasesFireStoreRepository releasesFireStoreRepository;

    public List<Release> findAllRelease(final int page, final int size, final List<String> categories) {
        final List<Release> releases = releasesFireStoreRepository.findAllWithPagination(page, size, categories);
        return releases;
    }
}
