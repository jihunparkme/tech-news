package com.technews.aggregate.posts.service

import com.technews.aggregate.posts.domain.Post
import com.technews.aggregate.posts.domain.repository.PostsRepository
import com.technews.aggregate.posts.dto.SavePostRequest
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {}

@Service
class PostsSchedulerService(
    private val postsRepository: PostsRepository,
) {
    @Transactional
    fun insertPost(savePostRequest: SavePostRequest) {
        return try {
            postsRepository.save(savePostRequest.toPost())
            logger.info("add new post. ${savePostRequest.title}")
        } catch (e: Exception) {
            logger.error("SpringBlogsSchedulerService.insertPost exception", e)
        }
    }

    @Transactional(readOnly = true)
    fun findLastPost(category: String): Post =
        postsRepository.findByCategoryOrderByDateDescLimitOne(category).firstOrNull() ?: Post()

    fun isNotExistOracleJavaPosts(title: String): Boolean =
        postsRepository.findByTitle(title).isEmpty()
}
