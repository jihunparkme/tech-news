package com.technews.scheduler;

import com.technews.aggregate.posts.constant.JavaBlogsSubject;
import com.technews.aggregate.posts.constant.PostSubjects;
import com.technews.aggregate.posts.dto.SavePostRequest;
import com.technews.aggregate.posts.service.PostsSchedulerService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlogOracleJavaScheduler {

    private static final String BLOG_BASE_URL = "https://blogs.oracle.com/java/";
    private static final List<String> CATEGORIES = List.of("Product & Ecosystem", "Java Technology");

    private static Set<String> SAVED_POST_TITLE = new HashSet<>();

    private final PostsSchedulerService postsSchedulerService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void runScheduled() {
        searchOracleJavaBlogPosts();
    }

    private void searchOracleJavaBlogPosts() {
        final List<OracleJavaBlogPost> oracleJavaBlogPosts = getPosts();
        oracleJavaBlogPosts.stream()
                .filter(post -> postsSchedulerService.isNotExistOracleJavaPosts(post.getTitle()))
                .map(post -> post.toSavePostRequest())
                .forEach(post -> postsSchedulerService.insertPost(post));
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
                    .subject(PostSubjects.JAVA.value())
                    .title(postInfo.title())
                    .url(postInfo.url())
                    .category(JavaBlogsSubject.ORACLE.value())
                    .writer(postInfo.writer())
                    .date(LocalDate.now().toString())
                    .tags(List.of(JavaBlogsSubject.ORACLE.value(), category))
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

        public SavePostRequest toSavePostRequest() {
            return SavePostRequest.builder()
                    .subject(this.subject)
                    .title(this.title)
                    .url(this.url)
                    .category(this.category)
                    .writer(this.writer)
                    .date(this.date)
                    .tags(this.tags)
                    .createdDt(this.createdDt)
                    .build();
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
