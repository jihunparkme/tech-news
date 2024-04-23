package com.technews.aggregate.releases.springframework.service;

import com.technews.aggregate.releases.springframework.domain.Release;
import com.technews.aggregate.releases.springframework.domain.repository.ReleasesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReleasesService {

    private final ReleasesRepository releasesRepository;

    public List<Release> findAllRelease(final int page, final int size) {
        final List<Release> releases = releasesRepository.findAllWithPagination(page, size);
        return releases;
    }
}
