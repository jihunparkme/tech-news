package com.technews.aggregate.releases.springframework.service;

import com.technews.aggregate.releases.springframework.domain.Release;
import com.technews.aggregate.releases.springframework.domain.repository.ReleasesFireStoreRepository;
import com.technews.aggregate.releases.springframework.dto.SaveReleaseRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReleasesSchedulerService {

    private final ReleasesFireStoreRepository releasesFireStoreRepository;

    public void insertRelease(SaveReleaseRequest saveReleaseRequest) {
        try {
            releasesFireStoreRepository.save(saveReleaseRequest);
            log.info("add new release version. {}", saveReleaseRequest.getVersion());
        } catch (Exception e) {
            log.error("ReleasesSchedulerService.insertRelease exception", e);
        }
    }

    public Release findLatestRelease(final String project) {
        final List<Release> latestReleaseDate = releasesFireStoreRepository.findLatestReleaseDate(project);
        if (latestReleaseDate.isEmpty()) {
            return Release.EMPTY;
        }

        return latestReleaseDate.get(0);
    }
}
