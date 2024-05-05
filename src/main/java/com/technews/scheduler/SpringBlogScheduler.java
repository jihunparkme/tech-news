package com.technews.scheduler;

import com.technews.aggregate.posts.constant.BlogSubjects;
import com.technews.aggregate.posts.domain.Post;
import com.technews.aggregate.posts.spring.constant.SpringBlogsSubject;
import com.technews.aggregate.posts.spring.dto.SavePostRequest;
import com.technews.aggregate.posts.spring.service.SpringBlogsSchedulerService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpringBlogScheduler {

    private final static DateTimeFormatter englishFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.ENGLISH);
    private final static DateTimeFormatter createdFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final String CATEGORY_URL = "https://spring.io/blog/category/";
    private static final String BLOG_URL = "https://spring.io";

    private final SpringBlogsSchedulerService springBlogsSchedulerService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void runScheduler() {
        searchSpringBlogPosts(SpringBlogsSubject.ENGINEERING);
        searchSpringBlogPosts(SpringBlogsSubject.RELEASES);
        searchSpringBlogPosts(SpringBlogsSubject.NEWS);
    }

    private void searchSpringBlogPosts(final SpringBlogsSubject subject) {
        final Post latestPost = springBlogsSchedulerService.findLatestPost(subject.value());
        final List<SavePostRequest> posts = getPostInfo(subject.value());
        posts.stream()
                .filter(post -> post.isLatestDatePost(latestPost.getDate()))
                .forEach(post -> springBlogsSchedulerService.insertPost(post));
    }

    private static List<SavePostRequest> getPostInfo(final String category) {
        final List<SavePostRequest> result = new ArrayList<>();

        try {
            final Document doc = Jsoup.connect(CATEGORY_URL + category).get();
            final Elements elements = doc.select(".blog-post");
            for (Element element : elements) {
                final String title = getTitle(element);
                final Meta meta = getMeta(element);
                final String url = getPostUrl(element);

                result.add(SavePostRequest.builder()
                        .subject(BlogSubjects.SPRING.value())
                        .title(title)
                        .url(url)
                        .category(category)
                        .writer(meta.writer())
                        .date(meta.date())
                        .tags(List.of(BlogSubjects.SPRING.value(), category))
                        .createdDt(LocalDate.now().format(createdFormatter))
                        .build());
            }
        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        }

        return result;
    }

    private static String getTitle(final Element element) {
        try {
            return element.select(".has-text-weight-medium").text();
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

    private static Meta getMeta(final Element element) {
        final String meta = element.select(".meta").text();
        final String[] metas = meta.split("\\|");
        if (metas.length == 0) {
            return Meta.EMPTY;
        }

        try {
            return Meta.builder()
                    .category(metas[0].trim())
                    .writer(metas[1].trim())
                    .date(getFormattedDate(metas[2].trim()))
                    .build();
        } catch (Exception e) {
            return Meta.EMPTY;
        }
    }

    private static String getFormattedDate(final String date) {
        try {
            final LocalDate localDate = LocalDate.parse(date, englishFormatter);
            return localDate.format(createdFormatter);
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

    private static String getPostUrl(final Element element) {
        try {
            final String url = element.select(".button").attr("href");
            return BLOG_URL + url;
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

    private record Meta(
            String category,
            String writer,
            String date
    ) {
        public static final Meta EMPTY = new Meta(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);

        @Builder
        private Meta {
        }
    }
}
