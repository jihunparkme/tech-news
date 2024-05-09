package com.technews.common.util;

import com.technews.aggregate.posts.domain.Post;
import com.technews.aggregate.releases.domain.Release;
import com.technews.aggregate.subscribe.dto.SubscriberMailContents;
import org.junit.jupiter.api.Test;

import java.util.List;

class MailTemplateUtilsTest {

    @Test 
    void generateContents() {
        final Release springRelease = Release.builder()
                .id("663b41125a7bf3409de3b126")
                .project("spring-framework")
                .version("Release v6.2.0-M1")
                .date("Apr 11, 2024")
                .url("https://github.com/spring-projects/spring-framework/releases/tag/v6.2.0-M1")
                .tags(List.of("spring-framework", "release"))
                .shared(false)
                .createdDt("2024-04-11")
                .build();

        final Release javaRelease = Release.builder()
                .id("663b41145a7bf3409de3b14e")
                .project("JDK 8")
                .version("JDK 8u92")
                .url("https://github.com/spring-projects/spring-framework/releases/tag/v6.2.0-M1")
                .tags(List.of("java", "jdk 8"))
                .shared(false)
                .createdDt("2024-05-08")
                .build();

        final Post springPost = Post.builder()
                .id("663b41155a7bf3409de3b1b7")
                .subject("Spring")
                .title("Spring Tips: Vector Databases with Spring AI")
                .url("https://spring.io/blog/2024/05/07/spring-tips-vector-databases-with-spring-ai")
                .category("engineering")
                .writer("Josh Long")
                .date("2024-05-07")
                .tags(List.of("spring"))
                .shared(false)
                .createdDt("2024-05-08")
                .build();

        final Post javaPost = Post.builder()
                .id("663b411b5a7bf3409de3b1d5")
                .subject("Java")
                .title("JEP targeted to JDK 23: 474: ZGC: Generational Mode by Default")
                .url("https://openjdk.org/jeps/474")
                .category("inside")
                .writer("Axel Boldt-Christmas")
                .date("2024-05-07")
                .tags(List.of("java"))
                .shared(false)
                .createdDt("2024-05-08")
                .build();

        final SubscriberMailContents subscriberMailContents = SubscriberMailContents.builder()
                .springReleases(List.of(springRelease, springRelease))
                .javaReleases(List.of(javaRelease, javaRelease))
                .springPosts(List.of(springPost, springPost))
                .javaPosts(List.of(javaPost, javaPost))
                .build();

        final String contents = MailTemplateUtils.generateContents(subscriberMailContents);
        System.out.println(contents);
    }
}