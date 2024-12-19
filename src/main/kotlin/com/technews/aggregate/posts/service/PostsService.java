package com.technews.aggregate.posts.service;

import com.technews.aggregate.posts.constant.PostSubjects;
import com.technews.aggregate.posts.domain.Post;
import com.technews.aggregate.posts.domain.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional(readOnly = true)
    public Page<Post> findAllRelease(final PostSubjects subject, final PageRequest pageable, final List<String> categories) {
        return getReleasePage(subject, categories, pageable);
    }

    private Page<Post> getReleasePage(final PostSubjects subject, final List<String> categories, final PageRequest pageable) {
        if (CollectionUtils.isEmpty(categories)) {
            return postsRepository.findBySubject(subject.value(), pageable);
        }

        return postsRepository.findBySubjectAndCategoryIn(subject.value(), categories, pageable);
    }
}
