package com.technews.aggregate.posts.dto

import com.technews.aggregate.posts.domain.Post
import com.technews.common.util.DateUtils
import mu.KotlinLogging
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

class SavePostRequest(
    val subject: String,
    val title: String,
    val url: String,
    val category: String,
    val writer: String,
    val date: String,
    var tags: List<String>,
    val createdDt: String,
) {

    fun isLatestDatePost(latestPostDate: String): Boolean =
        when {
            date.isBlank() || latestPostDate.isBlank() -> true
            else -> runCatching {
                val latest = LocalDate.parse(latestPostDate, DateUtils.CREATED_FORMATTER)
                val parsedDate = LocalDate.parse(date, DateUtils.CREATED_FORMATTER)
                parsedDate.isAfter(latest)
            }.onFailure {
                logger.error { "Error parsing the date. date: $date, message: ${it.message}" }
            }.getOrDefault(false)
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
