package com.technews.aggregate.posts.dto

import com.technews.aggregate.posts.domain.Post
import com.technews.common.util.DateUtils
import mu.KotlinLogging
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

data class SavePostRequest(
    val subject: String = "",
    val title: String = "",
    val url: String = "",
    val category: String = "",
    val writer: String = "",
    val date: String = "",
    var tags: List<String> = emptyList(),
    val createdDt: String = "",
) {

    fun isLatestDatePost(latestPostDate: String): Boolean {
        if (date.isBlank() || latestPostDate.isBlank()) return true

        return try {
            val latest = LocalDate.parse(latestPostDate, DateUtils.CREATED_FORMATTER)
            val parsedDate = LocalDate.parse(date, DateUtils.CREATED_FORMATTER)
            parsedDate.isAfter(latest)
        } catch (e: Exception) {
            logger.error("Error parsing the date. date: $date, message: ${e.message}", e)
            false
        }
    }

    fun toPost(): Post = Post(
        subject = this.subject,
        title = this.title,
        category = this.category,
        writer = this.writer,
        date = this.date,
        tags = this.tags,
        url = this.url,
        shared = false,
        createdDt = this.createdDt,
    )
}
