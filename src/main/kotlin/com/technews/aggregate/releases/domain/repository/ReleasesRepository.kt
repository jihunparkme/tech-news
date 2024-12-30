package com.technews.aggregate.releases.domain.repository

import com.technews.aggregate.releases.domain.Release
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.MongoRepository

interface ReleasesRepository : MongoRepository<Release, String> {
    @Aggregation(
        pipeline = [
            "{ '\$match': { 'project' : ?0 } }",
            "{ '\$sort' : { 'version' : -1 } }",
            "{ '\$limit' : 1 }",
        ],
    )
    fun findByProjectOrderByVersionDescLimitOne(project: String): List<Release>

    override fun findAll(pageable: Pageable): Page<Release>

    fun findByProjectIn(project: List<String>?, pageable: Pageable): Page<Release>

    fun findBySharedFalse(): List<Release>
}
