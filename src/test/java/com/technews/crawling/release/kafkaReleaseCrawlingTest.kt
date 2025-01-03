package com.technews.crawling.release

import com.technews.aggregate.posts.constant.PostSubjects
import com.technews.common.util.DateUtils
import io.kotest.core.spec.style.BehaviorSpec
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.time.LocalDateTime

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

        private fun Element.toPostOrNull(): Releases? =
            runCatching {
                val version = text()
                println("version : $version")
                println("https://kafka.apache.org/downloads#$version")
                val postInfo = getPostInfo(version)

                Releases(
                    project = PostSubjects.KAFKA.value,
                    version = version,
                    publishDate = postInfo.first,
                    url = postInfo.second,
                )
            }.getOrElse {
                logger.error(it) { "Failed to parse post for version" }
                null
            }

        private fun Element.getPostInfo(version: String): Pair<String, String> {
            return runCatching {
                val listItems = nextElementSibling()
                    ?.selectFirst("ul")
                    ?.select("li") ?: emptyList()

                // TODO 날짜 포맷 변경 yyy-mm-dd
                val publishDate = getPublishDate(listItems)
                val url = listItems.getOrNull(1)
                    ?.selectFirst("a")
                    ?.attr("href")
                    ?: "https://archive.apache.org/dist/kafka/$version/RELEASE_NOTES.html"

                println("publishDate: $publishDate")
                println("url: $url")

                publishDate to url
            }.getOrElse {
                logger.warn(it) { "Failed to extract post info for version: $version" }
                DateUtils.today() to "https://archive.apache.org/dist/kafka/$version/RELEASE_NOTES.html"
            }
        }

        private fun getPublishDate(listItems: List<Element>) =
            listItems.getOrNull(0)?.text()?.takeIf { it.isNotBlank() } ?: DateUtils.today()

        private data class Releases(
            val project: String,
            var version: String,
            val publishDate: String,
            val url: String,
        )
    }
}
