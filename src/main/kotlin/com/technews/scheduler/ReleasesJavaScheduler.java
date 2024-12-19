package com.technews.scheduler;

import com.technews.aggregate.releases.domain.Release;
import com.technews.aggregate.releases.dto.SaveReleaseRequest;
import com.technews.aggregate.releases.service.ReleasesSchedulerService;
import com.technews.common.constant.JdkVersion;
import com.technews.common.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReleasesJavaScheduler {

    private static final String ORACLE_BASE_URL = "https://www.oracle.com";

    private final ReleasesSchedulerService releasesSchedulerService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void runScheduler() {
        searchJdkReleases(JdkVersion.JDK_8);
        searchJdkReleases(JdkVersion.JDK_11);
        searchJdkReleases(JdkVersion.JDK_17);
        searchJdkReleases(JdkVersion.JDK_21);
    }

    private void searchJdkReleases(final JdkVersion jdk) {
        final Release jdkLatestRelease = releasesSchedulerService.findLatestRelease(jdk.value());
        final List<SaveReleaseRequest> jdkRelease = getRelease(jdk);
        jdkRelease.stream()
                .filter(release -> release.isLatestVersion(jdkLatestRelease.getVersion()))
                .forEach(release -> releasesSchedulerService.insertRelease(release));
    }

    private static List<SaveReleaseRequest> getRelease(final JdkVersion jdk) {
        try {
            if (JdkVersion.JDK_8.equals(jdk)) {
                return getLowerJdk8ReleaseInfo(jdk.value(), jdk.url());
            }
            return getExceededJdk8ReleaseInfo(jdk.value(), jdk.url());
        } catch (Exception e) {
            log.error("{}", e.getMessage(), e);
            return Collections.EMPTY_LIST;
        }
    }

    private static List<SaveReleaseRequest> getExceededJdk8ReleaseInfo(final String project, final String url) throws IOException {
        List<SaveReleaseRequest> result = new ArrayList<>();

        final Document doc = Jsoup.connect(url).get();
        final Elements items = doc.select(".obullets");
        for (Element item : items) {
            final Elements liElements = item.select("li");
            result.addAll(getReleaseList(project, liElements));
        }
        return result.stream()
                .filter(SaveReleaseRequest::isNotEmpty)
                .sorted(Comparator.comparing(SaveReleaseRequest::getVersion).reversed())
                .collect(Collectors.toList());
    }

    private static List<SaveReleaseRequest> getLowerJdk8ReleaseInfo(final String project, final String url) throws IOException {
        List<SaveReleaseRequest> result = new ArrayList<>();

        final Document doc = Jsoup.connect(url).get();
        final Elements items = doc.select(".col-item");
        for (Element item : items) {
            final Elements liElements = item.select("li");
            result.addAll(getReleaseList(project, liElements));
        }
        return result.stream()
                .filter(SaveReleaseRequest::isNotEmpty)
                .sorted(Comparator.comparing(SaveReleaseRequest::getVersion).reversed())
                .collect(Collectors.toList());
    }

    private static List<SaveReleaseRequest> getReleaseList(final String project, final Elements liElements) {
        List<SaveReleaseRequest> result = new ArrayList<>();
        for (final Element li : liElements) {
            final SaveReleaseRequest release = getRelease(project, li);
            if (release.isEmpty()) {
                continue;
            }

            result.add(release);
        }
        return result;
    }

    private static SaveReleaseRequest getRelease(final String project, final Element li) {
        final String liText = li.text();
        if (!liText.contains("GA")) {
            return SaveReleaseRequest.EMPTY;
        }

        try {
            final String version = liText.split(" \\(")[0];
            final String href = li.select("a").first().attr("href");
            final String gaUrl = ORACLE_BASE_URL + href;
            return SaveReleaseRequest.builder()
                    .createdDt(LocalDate.now().format(DateUtils.CREATED_FORMATTER))
                    .tags(List.of(project, "java", "release"))
                    .project(project)
                    .version(version)
                    .url(gaUrl)
                    .build();
        } catch (Exception e) {
            log.error("Not found matching version.");
        }

        return SaveReleaseRequest.EMPTY;
    }
}
