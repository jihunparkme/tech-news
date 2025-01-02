package com.technews.crawling.release

import com.technews.aggregate.posts.constant.PostSubjects
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

                // TODO empty 를 today 로 처리
                // TODO 날짜 포맷 변경 yyy-mm-dd
                val publishDate = listItems.getOrNull(0)?.text().orEmpty()
                val url = listItems.getOrNull(1)
                    ?.selectFirst("a")
                    ?.attr("href")
                    ?: "https://archive.apache.org/dist/kafka/$version/RELEASE_NOTES.html"

                println("publishDate: $publishDate")
                println("url: $url")

                publishDate to url
            }.getOrElse {
                logger.warn(it) { "Failed to extract post info for version: $version" }
                LocalDateTime.now().toString() to "https://archive.apache.org/dist/kafka/$version/RELEASE_NOTES.html"
            }



            // val select = it.nextElementSibling().select("ul").select("li")
            // runCatching {
            //     val publishDate = select.getOrNull(0) ?: LocalDateTime.now().toString()
            //     println(publishDate)
            //     val orElse = select.getOrNull(1)
            //     val url = (orElse == null) ? "https://archive.apache.org/dist/kafka/$version/RELEASE_NOTES.html" : orElse.select("a").attr("href")
            //     println(url)
            //     return Pair(publishDate, url)
            // }.getOrElse {
            //     println("https://archive.apache.org/dist/kafka/$version/RELEASE_NOTES.html")
            //
            //     return Pair(
            //         LocalDateTime.now().toString(),
            //         "https://archive.apache.org/dist/kafka/$version/RELEASE_NOTES.html"
            //     )
            // }
        }

        private data class Releases(
            val project: String,
            var version: String,
            val publishDate: String,
            val url: String,
        )
    }
}
