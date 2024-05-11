package com.technews.scheduler;

import com.technews.aggregate.posts.constant.PostSubjects;
import com.technews.aggregate.posts.domain.Post;
import com.technews.aggregate.posts.constant.JavaBlogsSubject;
import com.technews.aggregate.posts.dto.SavePostRequest;
import com.technews.aggregate.posts.service.PostsSchedulerService;
import com.technews.common.util.DateUtils;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlogJavaInsideScheduler {

    private static final String BLOG_URL = "https://inside.java";

    private final PostsSchedulerService postsSchedulerService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void runScheduled() {
        searchJavaBlogPosts(JavaBlogsSubject.INSIDE);
    }

    private void searchJavaBlogPosts(final JavaBlogsSubject subject) {
        final Post latestPost = postsSchedulerService.findLatestPost(subject.value());
        final List<SavePostRequest> posts = getPosts();
        posts.stream()
                .filter(post -> post.isLatestDatePost(latestPost.getDate()))
                .forEach(post -> postsSchedulerService.insertPost(post));
    }

    private static List<SavePostRequest> getPosts() {
        final List<SavePostRequest> result = new ArrayList<>();

        try {
            final Document doc = Jsoup.connect(BLOG_URL).get();
            final Elements postElements = doc.select(".post");
            for (Element postElement : postElements) {
                result.add(getPost(postElement));
            }
        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        }

        return result;
    }

    private static SavePostRequest getPost(final Element postElement) {
        final SavePostRequest.SavePostRequestBuilder savePostBuilder = SavePostRequest.builder();
        savePostBuilder.subject(PostSubjects.JAVA.value());
        savePostBuilder.category(JavaBlogsSubject.INSIDE.value());

        savePostBuilder.title(getTitle(postElement));
        savePostBuilder.url(getUrl(postElement));

        final PostInfo postInfo = getPostInfo(postElement);
        savePostBuilder.writer(postInfo.writer());
        savePostBuilder.date(DateUtils.getFormattedDate(postInfo.date()));

        final List<String> tags = getTags(postElement);
        savePostBuilder.tags(tags);

        savePostBuilder.createdDt(LocalDate.now().format(DateUtils.CREATED_FORMATTER));

        return savePostBuilder.build();
    }

    private static String getTitle(final Element postElement) {
        try {
            return postElement.select(".post-title").text();
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

    private static String getUrl(final Element postElement) {
        try {
            String url = postElement.select("a").first().attr("href");
            if (url.contains("http")) {
                return url;
            }

            return BLOG_URL + url;
        } catch (Exception e) {
            return BLOG_URL;
        }
    }

    private static PostInfo getPostInfo(final Element postElement) {
        final PostInfo.PostInfoBuilder postInfoBuilder = PostInfo.builder();

        final String info = postElement.select(".post-info").text();
        try {
            final String[] split = info.split(" on ");
            if (split.length == 2) {
                postInfoBuilder.writer(split[0]);
                postInfoBuilder.date(split[1]);
                return postInfoBuilder.build();
            }

            postInfoBuilder.date(split[0]);
            return postInfoBuilder.build();
        } catch (Exception e) {
            return PostInfo.EMPTY;
        }
    }

    private static List<String> getTags(final Element postElement) {
        final Element first = postElement.select("span#post-tags").first();
        final Elements tagElements = first.select(".tag-small");

        final List<String> tags = new ArrayList<>();
        for (final Element tagElement : tagElements) {
            tags.add(tagElement.text());
        }
        return tags;
    }

    private record PostInfo(
            String writer,
            String date
    ) {
        public static final PostInfo EMPTY = new PostInfo(StringUtils.EMPTY, StringUtils.EMPTY);

        @Builder
        private PostInfo {
        }
    }
}
