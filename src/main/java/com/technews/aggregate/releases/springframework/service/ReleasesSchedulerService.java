package com.technews.aggregate.releases.springframework.service;

import com.technews.aggregate.releases.springframework.domain.Release;
import com.technews.aggregate.releases.springframework.domain.repository.ReleasesRepository;
import com.technews.aggregate.releases.springframework.dto.SaveReleaseRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReleasesSchedulerService {

    private final ReleasesRepository releasesRepository;

    public void insertRelease(SaveReleaseRequest saveReleaseRequest) {
        try {
            releasesRepository.save(saveReleaseRequest);
            log.info("add new release version. {}", saveReleaseRequest.getVersion());
        } catch (Exception e) {
            log.error("ReleasesSchedulerService.insertRelease exception", e);
        }
    }

    public Release findLatestRelease(final String project) {
        final List<Release> latestReleaseDate = releasesRepository.findLatestReleaseDate(project);
        if (latestReleaseDate.isEmpty()) {
            return Release.EMPTY;
        }

        return latestReleaseDate.get(0);
    }
}
