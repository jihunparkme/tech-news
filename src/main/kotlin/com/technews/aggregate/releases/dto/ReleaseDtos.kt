package com.technews.aggregate.releases.dto

import com.technews.aggregate.releases.domain.Release
import mu.KotlinLogging
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

private val logger = KotlinLogging.logger {}

data class SaveReleaseRequest(
    val project: String = "",
    val version: String = "",
    val date: String = "",
    val url: String = "",
    val tags: List<String> = emptyList(),
    val createdDt: String = "",
) {
    companion object {
        val EMPTY: SaveReleaseRequest = SaveReleaseRequest()

        private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)
    }

    fun toRelease(): Release =
        Release(
            project = project,
            version = version,
            date = date,
            url = url,
            tags = tags,
            createdDt = createdDt,
        )

    fun isLatestDateVersion(latestReleaseDate: String): Boolean {
        if (latestReleaseDate.isBlank()) return true

        return try {
            val latest = LocalDate.parse(latestReleaseDate, formatter)
            val date = LocalDate.parse(this.date, formatter)
            date.isAfter(latest)
        } catch (e: DateTimeParseException) {
            logger.error("Error parsing the date. date: ${this.date}, message: ${e.message}", e)
            false
        }
    }

    fun isLatestVersion(version: String): Boolean {
        if (version.isBlank()) return true
        return this.version.compareTo(other = version, ignoreCase = true) > 0
    }

    val isEmpty: Boolean
        get() = this === EMPTY

    val isNotEmpty: Boolean
        get() = !this.isEmpty
}

data class ReleaseResponse(
    val id: String = "",
    val project: String = "",
    val version: String = "",
    val date: String = "",
    val url: String = "",
    val tags: List<String> = emptyList(),
    var shared: Boolean = false,
    val createdDt: String = "",
)