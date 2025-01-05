package com.technews.crawling.release

import com.technews.common.dto.Project
import com.technews.common.util.DateUtils
import io.kotest.core.spec.style.BehaviorSpec
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

private val logger = KotlinLogging.logger {}

class kafkaReleaseCrawlingTest : BehaviorSpec({
    Given("kafka releases 정보 크롤링") {
        Jsoup.connect(BASE_RELEASES_URL)
            .get()
            .select(".download-version")
            .mapNotNull { it.toPostOrNull() }
            .forEach { println(it) }
    }
}) {
    companion object {
        private const val BASE_RELEASES_URL = "https://kafka.apache.org/downloads"
        private val monthMap = mapOf(
            "Sept" to "September",
            "Jul" to "July",
            "Jun" to "June",
        )

        private fun Element.toPostOrNull(): Releases? = runCatching {
            val version = text()
            println("version : $version")
            println("https://kafka.apache.org/downloads#$version")
            val postInfo = getPostInfo(version)

            Releases(
                project = Project.KAFKA.value,
                version = version,
                publishDate = postInfo.first,
                url = postInfo.second,
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

            println("publishDate: $publishDate")
            println("url: $url")

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
                            .let { DateUtils.parseEnglishDateFormat(it) }
                    }.getOrElse {
                        DateUtils.today()
                    }
                } ?: DateUtils.today()

        private data class Releases(
            val project: String,
            var version: String,
            val publishDate: String,
            val url: String,
        )
    }
}
