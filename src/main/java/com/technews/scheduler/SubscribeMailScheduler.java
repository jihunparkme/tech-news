package com.technews.scheduler;

import com.technews.aggregate.posts.constant.PostSubjects;
import com.technews.aggregate.posts.domain.Post;
import com.technews.aggregate.posts.domain.repository.PostsRepository;
import com.technews.aggregate.releases.domain.Release;
import com.technews.aggregate.releases.domain.repository.ReleasesRepository;
import com.technews.aggregate.subscribe.dto.SubscriberMailContents;
import com.technews.aggregate.subscribe.service.SubscribeService;
import com.technews.common.config.event.Events;
import com.technews.common.constant.JdkVersion;
import com.technews.common.constant.SpringRepository;
import com.technews.common.event.dto.SendMailEvent;
import com.technews.common.util.MailTemplateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscribeMailScheduler {

    private final static String SUBJECT = "Java & Spring News";

    private final ReleasesRepository releasesRepository;
    private final PostsRepository postsRepository;
    private final SubscribeService subscribeService;

    @Scheduled(cron = "0 0 9 * * *")
    public void runScheduler() {
        final List<Release> releases = releasesRepository.findBySharedFalse();
        final List<Post> posts = postsRepository.findBySharedFalse();
        SubscriberMailContents subscriberMailContents = getSubscriberMailContents(releases, posts);

        final List<String> subscriber = subscribeService.findSubscriberEmail();
        Events.raise(SendMailEvent.builder()
                .subject(SUBJECT)
                .contents(MailTemplateUtils.generateContents())
                .addressList(Optional.of(subscriber))
                .build());
    }

    private SubscriberMailContents getSubscriberMailContents(final List<Release> releases, final List<Post> posts) {
        List<Release> releaseOfSpring = getReleaseOfSpring(releases);
        List<Release> releaseOfJava = getReleaseOfJava(releases);
        List<Post> postOfSpring = getPostOfSpring(posts);
        List<Post> postOfJava = getPostOfJava(posts);

        return SubscriberMailContents.builder()
                .releaseOfSpring(releaseOfSpring)
                .releaseOfJava(releaseOfJava)
                .postOfSpring(postOfSpring)
                .postOfJava(postOfJava)
                .build();
    }

    private List<Release> getReleaseOfSpring(final List<Release> releases) {
        return releases.stream()
                .filter(release -> SpringRepository.list().contains(release.getProject()))
                .collect(Collectors.toList());
    }

    private List<Release> getReleaseOfJava(final List<Release> releases) {
        return releases.stream()
                .filter(release -> JdkVersion.list().contains(release.getProject()))
                .collect(Collectors.toList());
    }

    private List<Post> getPostOfSpring(final List<Post> posts) {
        return posts.stream()
                .filter(post -> PostSubjects.SPRING.value().equals(post.getSubject()))
                .collect(Collectors.toList());
    }

    private List<Post> getPostOfJava(final List<Post> posts) {
        return posts.stream()
                .filter(post -> PostSubjects.JAVA.value().equals(post.getSubject()))
                .collect(Collectors.toList());
    }
}
