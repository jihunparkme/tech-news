package com.technews.crawling;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

@Slf4j
public class OracleJavaBlogCrawlingTest {

    private static final String BLOG_BASE_URL = "https://blogs.oracle.com/java/";
    private static final List<String> CATEGORIES = List.of("Product & Ecosystem", "Java Technology");

    private static Set<String> SAVED_POST_TITLE = new HashSet<>();

    @Test
    void test() {
        final List<OracleJavaBlogPost> posts = getPosts();
        System.out.println("posts.size() = " + posts.size());
        posts.forEach(System.out::println);
    }

    private static List<OracleJavaBlogPost> getPosts() {
        final List<OracleJavaBlogPost> result = new ArrayList<>();

        try {
            final Document doc = Jsoup.connect(BLOG_BASE_URL)
                    .header("User-Agent", "PostmanRuntime/7.37.3")
                    .get();
            final Elements categoryElements = doc.select(".with-category");
            for (Element categoryElement : categoryElements) {
                final String category = categoryElement.select(".rw-ptitle").text();
                if (CATEGORIES.contains(category)) {
                    result.addAll(getPosts(category, categoryElement));
                }
            }
        } catch (Exception e) {
            log.error("jsoup parsing exception. message: {}", e.getMessage(), e);
            return Collections.EMPTY_LIST;
        }

        return result;
    }

    private static List<OracleJavaBlogPost> getPosts(final String category, final Element postElement) {
        final List<OracleJavaBlogPost> result = new ArrayList<>();

        final Elements select = postElement.select(".cscroll-item-w1");
        for (final Element element : select) {
            final PostInfo postInfo = getPostInfo(element);
            if (SAVED_POST_TITLE.contains(postInfo.title())) {
                continue;
            }

            final OracleJavaBlogPost post = OracleJavaBlogPost.builder()
                    .subject("java")
                    .title(postInfo.title())
                    .url(postInfo.url())
                    .category("oracle")
                    .writer(postInfo.writer())
                    .date(LocalDate.now().toString())
                    .tags(List.of("oracle", category))
                    .createdDt(LocalDate.now().toString())
                    .build();

            result.add(post);
            SAVED_POST_TITLE.add(postInfo.title());
        }

        return result;
    }

    private static PostInfo getPostInfo(final Element element) {
        try {
            final Elements blogTile = element.select(".blogtile-w2");
            final Elements post = blogTile.select("a");
            return PostInfo.builder()
                    .title(post.get(2).text())
                    .url(BLOG_BASE_URL + post.get(2).attr("href"))
                    .writer(post.get(3).text())
                    .build();
        } catch (Exception e) {
            return PostInfo.EMPTY;
        }
    }

    @Getter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    private static class OracleJavaBlogPost {
        private String subject;
        private String title;
        private String url;
        private String category;
        private String writer;
        private String date;
        List<String> tags;
        private String createdDt;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OracleJavaBlogPost post = (OracleJavaBlogPost) o;
            return title == post.title;
        }

        @Override
        public int hashCode() {
            return Objects.hash(title);
        }
    }

    private record PostInfo(
            String title,
            String url,
            String writer
    ) {
        public static final PostInfo EMPTY = new PostInfo(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);

        @Builder
        private PostInfo {
        }
    }
}