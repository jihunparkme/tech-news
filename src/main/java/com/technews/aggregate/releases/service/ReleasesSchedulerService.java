package com.technews.aggregate.releases.service;

import com.technews.aggregate.releases.domain.Release;
import com.technews.aggregate.releases.domain.repository.ReleasesRepository;
import com.technews.aggregate.releases.dto.SaveReleaseRequest;
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
            releasesRepository.save(saveReleaseRequest.toRelease());
            log.info("add new release version. {}", saveReleaseRequest.getVersion());
        } catch (Exception e) {
            log.error("ReleasesSchedulerService.insertRelease exception", e);
        }
    }

    public Release findLatestRelease(final String project) {
        final List<Release> latestReleaseDate = releasesRepository.findByProjectOrderByVersionDescLimitOne(project);
        if (latestReleaseDate.isEmpty()) {
            return Release.EMPTY;
        }

        return latestReleaseDate.get(0);
    }
}
