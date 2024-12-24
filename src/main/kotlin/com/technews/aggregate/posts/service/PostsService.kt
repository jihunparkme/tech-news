package com.technews.aggregate.posts.service

import com.technews.aggregate.posts.constant.PostSubjects
import com.technews.aggregate.posts.domain.Post
import com.technews.aggregate.posts.domain.repository.PostsRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostsService(
    private val postsRepository: PostsRepository
) {
    @Transactional(readOnly = true)
    fun findAllRelease(
        subject: PostSubjects,
        pageable: PageRequest,
        categories: List<String>
    ): Page<Post> =
        if (categories.isEmpty()) {
            postsRepository.findBySubject(subject.value, pageable)
        } else {
            postsRepository.findBySubjectAndCategoryIn(subject.value, categories, pageable)
        }
}
