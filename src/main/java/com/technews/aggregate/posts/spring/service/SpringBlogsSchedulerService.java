package com.technews.aggregate.posts.spring.service;

import com.technews.aggregate.posts.domain.Post;
import com.technews.aggregate.posts.domain.repository.PostsRepository;
import com.technews.aggregate.posts.spring.dto.SaveSpringPostRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpringBlogsSchedulerService {

    private final PostsRepository postsRepository;

    public void insertPost(SaveSpringPostRequest saveSpringPostRequest) {
        try {
            postsRepository.save(saveSpringPostRequest.toRelease());
            log.info("add new post. {}", saveSpringPostRequest.getTitle());
        } catch (Exception e) {
            log.error("SpringBlogsSchedulerService.insertPost exception", e);
        }
    }

    public Post findLatestPost(final String category) {
        final List<Post> latestPost = postsRepository.findByCategoryOrderByDateDescLimitOne(category);
        if (latestPost.isEmpty()) {
            return Post.EMPTY;
        }

        return latestPost.get(0);
    }
}
