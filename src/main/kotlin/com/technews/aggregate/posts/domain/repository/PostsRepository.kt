package com.technews.aggregate.posts.domain.repository

import com.technews.aggregate.posts.domain.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.MongoRepository

interface PostsRepository : MongoRepository<Post, String> {
    @Aggregation(
        pipeline = [
            "{ '\$match': { 'category' : ?0 } }",
            "{ '\$sort' : { 'date' : -1 } }",
            "{ '\$limit' : 1 }"
        ]
    )
    fun findByCategoryOrderByDateDescLimitOne(category: String): List<Post>

    fun findBySubject(subject: String, pageable: PageRequest): Page<Post>

    fun findBySubjectAndCategoryIn(subject: String, category: List<String>?, pageable: PageRequest): Page<Post>

    fun findBySharedFalse(): List<Post>

    fun findByTitle(title: String): List<Post>
}
