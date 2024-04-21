package com.technews.crawling;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JavaReleaseNotesCrawlingTest {
    private static final String ORACLE_BASE_URL = "https://www.oracle.com/";
    private static final String JDK_8 = "https://www.oracle.com/java/technologies/javase/8u-relnotes.html";
    private static final String JDK_11 = "https://www.oracle.com/java/technologies/javase/11u-relnotes.html";
    private static final String JDK_17 = "https://www.oracle.com/java/technologies/javase/17u-relnotes.html";
    private static final String JDK_21 = "https://www.oracle.com/java/technologies/javase/21u-relnotes.html";

    @Test
    void test() throws IOException {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> JDK_8");
        final List<Release> jdk8ReleaseInfo = getReleaseInfo(JDK_8);
        jdk8ReleaseInfo.stream().forEach(System.out::println);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> JDK_11");
        final List<Release> jdk11ReleaseInfo = getReleaseInfo(JDK_11);
        jdk11ReleaseInfo.stream().forEach(System.out::println);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> JDK_17");
        final List<Release> jdk17ReleaseInfo = getReleaseInfo(JDK_17);
        jdk17ReleaseInfo.stream().forEach(System.out::println);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> JDK_21");
        final List<Release> jdk21ReleaseInfo = getReleaseInfo(JDK_21);
        jdk21ReleaseInfo.stream().forEach(System.out::println);
    }

    private static List<Release> getReleaseInfo(String url) throws IOException {

        if (JDK_8.equals(url)) {
            return getLowerJdk8ReleaseInfo(url);
        }

        return getExceededJdk8ReleaseInfo(url);
    }

    private static List<Release> getExceededJdk8ReleaseInfo(final String url) throws IOException {
        List<Release> result = new ArrayList<>();

        final Document doc = Jsoup.connect(url).get();
        final Elements items = doc.select(".obullets");
        for (Element item : items) {
            final Elements liElements = item.select("li");
            result.addAll(getReleaseList(liElements));
        }
        return result.stream()
                .filter(Release::isNotEmpty)
                .sorted(Comparator.comparing(Release::version).reversed())
                .collect(Collectors.toList());
    }

    private static List<Release> getLowerJdk8ReleaseInfo(final String url) throws IOException {
        List<Release> result = new ArrayList<>();

        final Document doc = Jsoup.connect(url).get();
        final Elements items = doc.select(".col-item");
        for (Element item : items) {
            final Elements liElements = item.select("li");
            result.addAll(getReleaseList(liElements));
        }
        return result.stream()
                .filter(Release::isNotEmpty)
                .sorted(Comparator.comparing(Release::version).reversed())
                .collect(Collectors.toList());
    }

    private static List<Release> getReleaseList(final Elements liElements) {
        List<Release> result = new ArrayList<>();
        for (final Element li : liElements) {
            final Release release = getRelease(li);
            if (release.isEmpty()) {
                continue;
            }

            result.add(release);
        }
        return result;
    }

    private static Release getRelease(final Element li) {
        final String liText = li.text();
        if (!liText.contains("GA")) {
            return Release.EMPTY;
        }

        try {
            final String version = liText.split(" \\(")[0];
            final String href = li.select("a").first().attr("href");
            final String gaUrl = ORACLE_BASE_URL + href;
            return Release.builder()
                    .version(version)
                    .gaUrl(gaUrl)
                    .build();
        } catch (Exception e) {
            log.error("Not found matching version.");
        }

        return Release.EMPTY;
    }

    private record Release(
            String version,
            String gaUrl
    ) {
        public static final Release EMPTY = new Release(StringUtils.EMPTY, StringUtils.EMPTY);

        @Builder
        private Release {
        }

        public boolean isEmpty() {
            return this == EMPTY;
        }

        public boolean isNotEmpty() {
            return !this.isEmpty();
        }
    }
}
