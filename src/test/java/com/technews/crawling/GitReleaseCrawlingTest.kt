package com.technews.crawling

import io.kotest.core.spec.style.BehaviorSpec
import mu.KotlinLogging
import org.jsoup.Jsoup

private val logger = KotlinLogging.logger {}

class GitReleaseCrawlingTest : BehaviorSpec({
    Given("Spring Release 정보 크롤링") {
        val repositories = listOf(
            "spring-framework",
            "spring-boot",
            "spring-data-jpa",
            "spring-batch",
        )

        repositories.forEach { repository ->
            When(repository) {
                println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> $repository")
                val releaseInfoList = getGitHubReleaseTags(repository)
                releaseInfoList.forEach { println(it) }
            }
        }
    }
}) {
    companion object {
        private const val SPRING_PROJECT_REPOSITORY_URL = "https://github.com/spring-projects/"
        private const val SPRING_PROJECT_REPOSITORY_TAGS = "/tags"
        private const val RELEASE_NOTE_BASE_URL = "https://github.com/spring-projects/"
        private const val RELEASE_NOTE_POSTFIX = "/releases/tag/"

        private val RELEASE_VERSION_REGEX = Regex("^(.*?)(?=\\s+Toggle)")
        private val RELEASE_DATE_REGEX =
            Regex("\\b(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s(\\d{1,2}),\\s(\\d{4})")

        private fun getGitHubReleaseTags(repository: String): List<ReleaseInfo> {
            val url = "$SPRING_PROJECT_REPOSITORY_URL$repository$SPRING_PROJECT_REPOSITORY_TAGS"
            return runCatching {
                Jsoup.connect(url).get()
                    .select(".Box-row")
                    .mapNotNull { element -> element.text().trim().let { getReleaseInfo(repository, it) } }
            }.getOrElse {
                println("Failed to fetch release tags for $repository: ${it.message}")
                emptyList()
            }
        }

        private fun getReleaseInfo(repository: String, text: String): ReleaseInfo? {
            val versionMatch = RELEASE_VERSION_REGEX.find(text)
            val dateMatch = RELEASE_DATE_REGEX.find(text)
            if (versionMatch == null) {
                logger.error("Not found matching version for $repository")
                return null
            }

            return ReleaseInfo(
                project = repository,
                version = "Release ${versionMatch.value}",
                date = dateMatch?.value.orEmpty(),
                url = generateReleaseUrl(repository, versionMatch.groupValues[1]),
            )
        }

        private fun generateReleaseUrl(repository: String, version: String): String =
            "$RELEASE_NOTE_BASE_URL$repository$RELEASE_NOTE_POSTFIX$version"

        private data class ReleaseInfo(
            val project: String,
            val version: String,
            val date: String,
            val url: String,
        )
    }
}
