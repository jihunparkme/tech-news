package com.technews.aggregate.posts.dto

import com.technews.aggregate.posts.domain.Post
import com.technews.common.util.DateUtils
import mu.KotlinLogging
import java.time.LocalDate
import java.time.format.DateTimeParseException

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
    fun isLatestDatePost(lastPostDate: String): Boolean {
        if (date.isBlank() || lastPostDate.isBlank()) {
            logger.debug("One of the dates is blank. date: $date, lastPostDate: $lastPostDate")
            return true
        }

        return try {
            val lastPostLocalDate = LocalDate.parse(lastPostDate, DateUtils.CREATED_FORMATTER)
            val savedPostLocalDate = LocalDate.parse(date, DateUtils.CREATED_FORMATTER)
            savedPostLocalDate.isAfter(lastPostLocalDate)
        } catch (e: DateTimeParseException) {
            logger.error(
                "Invalid date format encountered. date: $date, lastPostDate: $lastPostDate, message: ${e.message}",
                e,
            )
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

data class PostResponse(
    val id: String = "",
    val subject: String = "",
    val title: String = "",
    val url: String = "",
    val category: String = "",
    val writer: String = "",
    val date: String = "",
    var tags: List<String> = emptyList(),
    var shared: Boolean = false,
    val createdDt: String = "",
)
