package com.technews.scheduler.releases

import com.technews.aggregate.releases.dto.SaveReleaseRequest
import com.technews.aggregate.releases.service.ReleasesSchedulerService
import com.technews.common.dto.Project
import com.technews.common.util.DateUtils
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

@Component
class ReleasesKafkaScheduler(
    private val releasesSchedulerService: ReleasesSchedulerService,
) {
    @Scheduled(cron = "0 0 1 * * ?")
    fun runSchedule() {
        saveKafkaReleases()
    }

    private fun saveKafkaReleases() {
        val lastRelease = releasesSchedulerService.findLatestRelease(Project.KAFKA.value)
        searchKafkaReleases()
            .filter { it.isLatestVersion(lastRelease.version) }
            .forEach { releasesSchedulerService.insertRelease(it) }
    }

    private fun searchKafkaReleases(): List<SaveReleaseRequest> =
        Jsoup.connect(BASE_RELEASES_URL)
            .get()
            .select(".download-version")
            .mapNotNull { it.toPostOrNull() }

    companion object {
        private const val BASE_RELEASES_URL = "https://kafka.apache.org/downloads"
        private val monthMap = mapOf(
            "Sept" to "September",
            "Jul" to "July",
            "Jun" to "June",
        )

        private fun Element.toPostOrNull(): SaveReleaseRequest? = runCatching {
            val version = text()
            val postInfo = getPostInfo(version)

            SaveReleaseRequest(
                project = Project.KAFKA.value,
                version = version,
                date = postInfo.first,
                url = postInfo.second,
                tags = listOf(Project.KAFKA.value, "Release"),
                createdDt = LocalDate.now().format(DateUtils.CREATED_FORMATTER),
            )
        }.getOrElse {
            logger.error(it) { "Failed to parse post for version" }
            null
        }

        private fun Element.getPostInfo(version: String): Pair<String, String> = runCatching {
            val listItems = nextElementSibling()
                ?.selectFirst("ul")
                ?.select("li")
                ?: emptyList()

            val publishDate = getPublishDate(listItems)
            val url = listItems.getOrNull(1)
                ?.selectFirst("a")
                ?.attr("href")
                ?: "https://archive.apache.org/dist/kafka/$version/RELEASE_NOTES.html"

            publishDate to url
        }.getOrElse {
            logger.error(it) { "Failed to extract post info for version: $version" }
            DateUtils.today() to "https://archive.apache.org/dist/kafka/$version/RELEASE_NOTES.html"
        }

        private fun getPublishDate(listItems: List<Element>): String =
            listItems.firstOrNull()
                ?.text()
                ?.takeIf { it.isNotBlank() }
                ?.let { date ->
                    runCatching {
                        date.split("Released ")[1]
                            .replace(Regex("(\\d+)(st|nd|rd|th|ed)"), "$1")
                            .replace(Regex("\\b[A-Za-z]*")) { matchResult ->
                                monthMap[matchResult.value] ?: matchResult.value
                            }
                            .let { DateUtils.getFormattedDate(it) }
                    }.getOrElse {
                        DateUtils.today()
                    }
                } ?: DateUtils.today()
    }
}
