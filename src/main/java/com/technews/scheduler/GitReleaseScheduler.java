package com.technews.scheduler;

import com.technews.aggregate.releases.domain.Release;
import com.technews.aggregate.releases.dto.SaveReleaseRequest;
import com.technews.aggregate.releases.service.ReleasesSchedulerService;
import com.technews.common.constant.SpringRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class GitReleaseScheduler {

    private final static DateTimeFormatter beforeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
    private final static DateTimeFormatter afterFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final String SPRING_PROJECT_REPOSITORY_URL = "https://github.com/spring-projects/";
    private static final String SPRING_PROJECT_REPOSITORY_TAGS = "/tags";
    private static final String RELEASE_NOTE_BASE_URL = "https://github.com/spring-projects/";
    private static final String RELEASE_NOTE_POSTFIX = "/releases/tag/";

    private static final String RELEASE_VERSION_REGEX = "^(.*?)(?=\\s+Toggle)";
    private static final String RELEASE_DATE = "\\b(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s(\\d{1,2}),\\s(\\d{4})";
    private static final Pattern RELEASE_VERSION_PATTERN = Pattern.compile(RELEASE_VERSION_REGEX);
    private static final Pattern RELEASE_DATE_PATTERN = Pattern.compile(RELEASE_DATE);

    private final ReleasesSchedulerService releasesSchedulerService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void runScheduler() {
        searchSpringFramework(SpringRepository.SPRING_FRAMEWORK);
        searchSpringFramework(SpringRepository.SPRING_BOOT);
        searchSpringFramework(SpringRepository.SPRING_DATA_JPA);
        searchSpringFramework(SpringRepository.SPRING_BATCH);
    }

    private void searchSpringFramework(final SpringRepository repository) {
        final Release springFrameworkLatestRelease = releasesSchedulerService.findLatestRelease(repository.value());
        final List<SaveReleaseRequest> springFrameworkReleaseInfo = getGitHubReleaseTags(repository.value());
        springFrameworkReleaseInfo.stream()
                .filter(release -> release.isLatestDateVersion(springFrameworkLatestRelease.getDate()))
                .forEach(release -> releasesSchedulerService.insertRelease(release));
    }

    private static List<SaveReleaseRequest> getGitHubReleaseTags(final String repository) {
        final String url = SPRING_PROJECT_REPOSITORY_URL + repository + SPRING_PROJECT_REPOSITORY_TAGS;
        try {
            final Document doc = Jsoup.connect(url).get();
            final Elements elements = doc.select(".Box-row");

            final List<SaveReleaseRequest> result = new ArrayList<>();
            for (Element element : elements) {
                final String trim = element.text().trim();
                final SaveReleaseRequest releaseInfo = getReleaseInfo(repository, trim);
                result.add(releaseInfo);
            }

            return result;
        } catch (IOException e) {
            log.error("Jsoup.connect exception. url: {}, {}", url, e.getMessage(), e);
        } catch (Exception e) {
            log.error("getGitHubReleaseTags exception. {}", e.getMessage(), e);
        }

        return Collections.EMPTY_LIST;
    }

    private static SaveReleaseRequest getReleaseInfo(final String repository, final String trim) {
        final SaveReleaseRequest.SaveReleaseRequestBuilder builder = SaveReleaseRequest.builder();
        builder.project(repository);
        builder.tags(List.of(repository, "release"));

        final Matcher releaseVersionMatcher = RELEASE_VERSION_PATTERN.matcher(trim);
        if (releaseVersionMatcher.find()) {
            builder.version("Release " + releaseVersionMatcher.group(0));
            builder.url(generateReleaseUrl(repository, releaseVersionMatcher.group(1)));
        } else {
            log.error("Not found matching version.");
        }

        final Matcher relaseDateMatcher = RELEASE_DATE_PATTERN.matcher(trim);
        if (relaseDateMatcher.find()) {
            final String date = relaseDateMatcher.group(0);
            builder.date(date);
            builder.createdDt(convertDate(date));
        }

        return builder.build();
    }

    static String convertDate(final String date) {
        try {
            final LocalDate convertedDate = LocalDate.parse(date, beforeFormatter);
            return convertedDate.format(afterFormatter);
        } catch (DateTimeParseException e) {
            return StringUtils.EMPTY;
        }
    }

    private static String generateReleaseUrl(final String repository, final String version) {
        return RELEASE_NOTE_BASE_URL + repository + RELEASE_NOTE_POSTFIX + version;
    }
}
