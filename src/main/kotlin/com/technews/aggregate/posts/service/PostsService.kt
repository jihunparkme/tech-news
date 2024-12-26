package com.technews.aggregate.posts.service

import com.technews.aggregate.posts.constant.PostSubjects
import com.technews.aggregate.posts.domain.Post
import com.technews.aggregate.posts.domain.repository.PostsRepository
import com.technews.aggregate.posts.dto.PostResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostsService(
    private val postsRepository: PostsRepository,
) {
    @Transactional(readOnly = true)
    fun findAllRelease(
        subject: PostSubjects,
        pageable: PageRequest,
        categories: List<String>? = emptyList(),
    ): Page<PostResponse> {
        val postPage = if (categories.isNullOrEmpty()) {
            postsRepository.findBySubject(subject.value, pageable)
        } else {
            postsRepository.findBySubjectAndCategoryIn(subject.value, categories, pageable)
        }

        return postPage.map { post ->
            PostResponse(
                id = post.id,
                subject = post.subject,
                title = post.title,
                url = post.url,
                category = post.category,
                writer = post.writer,
                date = post.date,
                tags = post.tags,
                shared = post.shared,
                createdDt = post.createdDt,
            )
        }
    }
}
