package com.technews.crawling;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class GitReleaseCrawlingTest {

    private static final String SPRING_PROJECT_REPOSITORY_URL = "https://github.com/spring-projects/";
    private static final String SPRING_PROJECT_REPOSITORY_TAGS = "/tags";
    private static final String RELEASE_NOTE_BASE_URL = "https://github.com/spring-projects/";
    private static final String RELEASE_NOTE_POSTFIX = "/releases/tag/";

    private static final String RELEASE_VERSION_REGEX = "^(.*?)(?=\\s+Toggle)";
    private static final String RELEASE_DATE = "\\b(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s(\\d{1,2}),\\s(\\d{4})";
    private static final Pattern RELEASE_VERSION_PATTERN = Pattern.compile(RELEASE_VERSION_REGEX);
    private static final Pattern RELEASE_DATE_PATTERN = Pattern.compile(RELEASE_DATE);

    @Test
    void test() throws IOException {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> spring-framework");
        final ArrayList<ReleaseInfo> springFrameworkReleaseInfo = getGitHubReleaseTags("spring-framework");
        springFrameworkReleaseInfo.stream().forEach(System.out::println);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> spring-boot");
        final ArrayList<ReleaseInfo> springBootReleaseInfo = getGitHubReleaseTags("spring-boot");
        springBootReleaseInfo.stream().forEach(System.out::println);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> spring-data-jpa");
        final ArrayList<ReleaseInfo> springDataJpaReleaseInfo = getGitHubReleaseTags("spring-data-jpa");
        springDataJpaReleaseInfo.stream().forEach(System.out::println);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> spring-batch");
        final ArrayList<ReleaseInfo> springBatchJpaReleaseInfo = getGitHubReleaseTags("spring-batch");
        springBatchJpaReleaseInfo.stream().forEach(System.out::println);
    }

    private static ArrayList<ReleaseInfo> getGitHubReleaseTags(final String repository) throws IOException {
        final ArrayList<ReleaseInfo> result = new ArrayList<>();
        final String url = SPRING_PROJECT_REPOSITORY_URL + repository + SPRING_PROJECT_REPOSITORY_TAGS;
        final Document doc = Jsoup.connect(url).get();

        final Elements elements = doc.select(".Box-row");
        for (Element element : elements) {
            final String trim = element.text().trim();
            System.out.println(trim);
            final ReleaseInfo releaseInfo = getReleaseInfo(repository, trim);
            result.add(releaseInfo);
        }

        return result;
    }

    private static ReleaseInfo getReleaseInfo(final String repository, final String trim) {
        final ReleaseInfo.ReleaseInfoBuilder builder = ReleaseInfo.builder();

        final Matcher releaseVersionMatcher = RELEASE_VERSION_PATTERN.matcher(trim);
        if (releaseVersionMatcher.find()) {
            builder.version("Release " + releaseVersionMatcher.group(0));
            builder.url(generateReleaseUrl(repository, releaseVersionMatcher.group(1)));
        } else {
            log.error("일치하는 버전이 없습니다.");
        }

        final Matcher relaseDateMatcher = RELEASE_DATE_PATTERN.matcher(trim);
        if (relaseDateMatcher.find()) {
            builder.date(relaseDateMatcher.group(0));
        }

        return builder.build();
    }

    private static String generateReleaseUrl(final String repository, final String version) {
        if ("spring-data-jpa".equals(repository)) {
            return RELEASE_NOTE_BASE_URL + repository + RELEASE_NOTE_POSTFIX + version;
        }

        return RELEASE_NOTE_BASE_URL + repository + RELEASE_NOTE_POSTFIX + version;
    }

    @Builder
    private record ReleaseInfo(
            String version,
            String date,
            String url
    ) {
    }
}

