package com.technews.aggregate.posts.spring.service;

import com.technews.aggregate.posts.constant.BlogSubjects;
import com.technews.aggregate.posts.domain.Post;
import com.technews.aggregate.posts.domain.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpringBlogsService {

    private final PostsRepository postsRepository;

    public Page<Post> findAllRelease(final PageRequest pageable, final List<String> categories) {
        return getReleasePage(categories, pageable);
    }

    private Page<Post> getReleasePage(final List<String> categories, final PageRequest pageable) {
        if (CollectionUtils.isEmpty(categories)) {
            return postsRepository.findBySubject(BlogSubjects.SPRING.value(), pageable);
        }

        return postsRepository.findBySubjectAndCategoryIn(BlogSubjects.SPRING.value(), categories, pageable);
    }
}
