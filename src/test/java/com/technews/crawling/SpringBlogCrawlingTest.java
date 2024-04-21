package com.technews.crawling;

import lombok.Builder;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpringBlogCrawlingTest {

    private static final String CATEGORY_URL = "https://spring.io/blog/category/";
    private static final String BLOG_URL = "https://spring.io";
    private static final String ENGINEERING = "engineering";
    private static final String RELEASES = "releases";
    private static final String NEWS = "news";

    @Test
    void test() throws IOException {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ENGINEERING");
        final List<Post> engineeringPosts = getPostInfo(ENGINEERING);
        engineeringPosts.stream().forEach(System.out::println);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> RELEASES");
        final List<Post> releasesPosts = getPostInfo(RELEASES);
        releasesPosts.stream().forEach(System.out::println);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> NEWS");
        final List<Post> newsPosts = getPostInfo(NEWS);
        newsPosts.stream().forEach(System.out::println);
    }

    private static List<Post> getPostInfo(final String category) throws IOException {
        final List<Post> result = new ArrayList<>();
        final Document doc = Jsoup.connect(CATEGORY_URL + category).get();

        final Elements elements = doc.select(".blog-post");
        for (Element element : elements) {
            final String title = getTitle(element);
            final Meta meta = getMeta(element);
            final String url = getPostUrl(element);

            result.add(Post.builder()
                    .title(title)
                    .meta(meta)
                    .url(url)
                    .build());
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
                    .category(metas[0])
                    .writer(metas[1])
                    .date(metas[2])
                    .build();
        } catch (Exception e) {
            return Meta.EMPTY;
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
            String date
    ) {
        public static final Meta EMPTY = new Meta(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);

        @Builder
        private Meta {
        }
    }
}