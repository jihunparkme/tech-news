package com.technews.crawling;

import lombok.Builder;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JavaNewsCrawlingTest {
    private static final String BLOG_URL = "https://inside.java";

    @Test
    void test() throws Exception {
        final Document doc = Jsoup.connect(BLOG_URL).get();

        final Elements postElements = doc.select(".post");
        final List<Post> posts = getPosts(postElements);

        posts.forEach(System.out::println);
    }

    private static List<Post> getPosts(final Elements postElements) {
        List<Post> result = new ArrayList<>();
        for (Element postElement : postElements) {
            result.add(getPost(postElement));
        }

        return result;
    }

    private static Post getPost(final Element postElement) {
        final Post.PostBuilder postBuilder = Post.builder();
        final Meta.MetaBuilder mataBuilder = Meta.builder();

        postBuilder.title(getTitle(postElement));
        postBuilder.url(getUrl(postElement));

        final PostInfo postInfo = getPostInfo(postElement);
        mataBuilder.writer(postInfo.writer());
        mataBuilder.date(postInfo.date());

        final List<String> tags = getTags(postElement);
        mataBuilder.tags(tags);

        postBuilder.meta(mataBuilder.build());
        final Post post = postBuilder.build();
        return post;
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

    private static String getUrl(final Element postElement) {
        String url = postElement.select("a").first().attr("href");
        if (url.contains("http")) {
            return url;
        }

        return BLOG_URL + url;
    }

    private static String getTitle(final Element postElement) {
        return postElement.select(".post-title").text();
    }

    private record Post(
            String title,
            Meta meta,
            String url
    ) {
        @Builder
        private Post {
        }
    }

    private record Meta(
            String category,
            String writer,
            String date,

            List<String> tags
    ) {
        public static final Meta EMPTY = new Meta(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, Collections.EMPTY_LIST);

        @Builder
        private Meta {
        }
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
