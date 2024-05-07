package com.technews.aggregate.releases.service;

import com.technews.aggregate.releases.domain.Release;
import com.technews.aggregate.releases.domain.repository.ReleasesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReleasesService {

    private final ReleasesRepository releasesRepository;

    @Transactional(readOnly = true)
    public Page<Release> findAllRelease(
            final int page, final int size, final List<String> categories) {
        final PageRequest pageable = PageRequest.of(
                page, size,
                Sort.by("createdDt").descending().and(Sort.by("version").descending()));

        return getReleasePage(categories, pageable);
    }

    private Page<Release> getReleasePage(final List<String> categories, final PageRequest pageable) {
        if (CollectionUtils.isEmpty(categories)) {
            return releasesRepository.findAll(pageable);
        }

        return releasesRepository.findByProjectIn(categories, pageable);
    }
}
