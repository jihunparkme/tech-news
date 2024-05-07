package com.technews.aggregate.posts.service;

import com.technews.aggregate.posts.domain.Post;
import com.technews.aggregate.posts.domain.repository.PostsRepository;
import com.technews.aggregate.posts.dto.SavePostRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostsSchedulerService {

    private final PostsRepository postsRepository;

    @Transactional
    public void insertPost(SavePostRequest savePostRequest) {
        try {
            postsRepository.save(savePostRequest.toPost());
            log.info("add new post. {}", savePostRequest.getTitle());
        } catch (Exception e) {
            log.error("SpringBlogsSchedulerService.insertPost exception", e);
        }
    }

    @Transactional(readOnly = true)
    public Post findLatestPost(final String category) {
        final List<Post> latestPost = postsRepository.findByCategoryOrderByDateDescLimitOne(category);
        if (latestPost.isEmpty()) {
            return Post.EMPTY;
        }

        return latestPost.get(0);
    }
}
