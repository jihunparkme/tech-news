package com.technews.aggregate.posts.spring.service;

import com.technews.aggregate.posts.domain.Post;
import com.technews.aggregate.posts.domain.repository.PostsRepository;
import com.technews.aggregate.posts.spring.dto.SavePostRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpringBlogsSchedulerService {

    private final PostsRepository postsRepository;

    public void insertPost(SavePostRequest savePostRequest) {
        try {
            postsRepository.save(savePostRequest.toRelease());
            log.info("add new post. {}", savePostRequest.getTitle());
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