package com.technews.scheduler

import com.technews.aggregate.releases.dto.SaveReleaseRequest
import com.technews.aggregate.releases.service.ReleasesSchedulerService
import com.technews.common.constant.SpringRepository
import com.technews.common.util.DateUtils
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.regex.Pattern
import java.util.regex.Pattern.compile

private val logger = KotlinLogging.logger {}

@Component
class ReleaseSpringProjectsScheduler(
    private val releasesSchedulerService: ReleasesSchedulerService,
) {
    @Scheduled(cron = "0 0 1 * * ?")
    fun runScheduler() {
        searchSpringFramework(SpringRepository.SPRING_FRAMEWORK)
        searchSpringFramework(SpringRepository.SPRING_BOOT)
        searchSpringFramework(SpringRepository.SPRING_DATA_JPA)
        searchSpringFramework(SpringRepository.SPRING_BATCH)
    }

    private fun searchSpringFramework(repository: SpringRepository) {
        val springFrameworkLatestRelease = releasesSchedulerService.findLatestRelease(repository.value)
        val springFrameworkReleaseInfo = getGitHubReleaseTags(repository.value)
        springFrameworkReleaseInfo
            .filter { it.isLatestDateVersion(springFrameworkLatestRelease.date) }
            .forEach { releasesSchedulerService.insertRelease(it) }
    }

    companion object {
        private const val SPRING_PROJECT_REPOSITORY_URL = "https://github.com/spring-projects/"
        private const val SPRING_PROJECT_REPOSITORY_TAGS = "/tags"
        private const val RELEASE_NOTE_BASE_URL = "https://github.com/spring-projects/"
        private const val RELEASE_NOTE_POSTFIX = "/releases/tag/"

        private const val RELEASE_VERSION_REGEX = "^(.*?)(?=\\s+Toggle)"
        private const val RELEASE_DATE = "\\b(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s(\\d{1,2}),\\s(\\d{4})"
        private val RELEASE_VERSION_PATTERN: Pattern = compile(RELEASE_VERSION_REGEX)
        private val RELEASE_DATE_PATTERN: Pattern = compile(RELEASE_DATE)

        private fun getGitHubReleaseTags(repository: String): List<SaveReleaseRequest> {
            val url = "$SPRING_PROJECT_REPOSITORY_URL$repository$SPRING_PROJECT_REPOSITORY_TAGS"
            return try {
                val doc = Jsoup.connect(url).get()
                val elements = doc.select(".Box-row")

                elements.mapNotNull { element ->
                    val trim = element.text().trim()
                    getReleaseInfo(repository, trim)
                }
            } catch (e: IOException) {
                logger.error("Jsoup.connect exception. url: $url, ${e.message}", e)
                emptyList()
            } catch (e: Exception) {
                logger.error("getGitHubReleaseTags exception. ${e.message}", e)
                emptyList()
            }
        }

        private fun getReleaseInfo(repository: String, trim: String): SaveReleaseRequest {
            val version = RELEASE_VERSION_PATTERN.matcher(trim).group(0)
            val url = version?.let { generateReleaseUrl(repository, it) }

            val date = RELEASE_DATE_PATTERN.matcher(trim).group(0)
            val createdDt = date?.let { DateUtils.getFormattedDate(it) }

            if (version == null || date == null) {
                logger.error("Not found matching version or date.")
            }

            return SaveReleaseRequest(
                project = repository,
                tags = listOf(repository, "release"),
                version = version?.let { "Release $it" } ?: "",
                url = url ?: "",
                date = date ?: "",
                createdDt = createdDt ?: "",
            )
        }

        private fun generateReleaseUrl(repository: String, version: String): String {
            return "$RELEASE_NOTE_BASE_URL$repository$RELEASE_NOTE_POSTFIX$version"
        }
    }
}
