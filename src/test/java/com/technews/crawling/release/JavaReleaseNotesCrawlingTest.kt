package com.technews.crawling.release

import com.technews.common.util.DateUtils
import io.kotest.core.spec.style.BehaviorSpec
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

class JavaReleaseNotesCrawlingTest : BehaviorSpec({
    Given("Java Release 정보 크롤링") {
        val jdks = mapOf(
            "jdk8" to JDK_8,
            "jdk11" to JDK_11,
            "jdk17" to JDK_17,
            "jdk21" to JDK_21,
            "jdk23" to JDK_23,
        )

        jdks.forEach { (project, url) ->
            println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> $project")
            val releaseInfo = getReleaseInfo(project, url)
            releaseInfo.forEach { println(it) }
        }
    }
}) {
    companion object {
        private const val ORACLE_BASE_URL = "https://www.oracle.com"
        private const val JDK_8 = "https://www.oracle.com/java/technologies/javase/8u-relnotes.html"
        private const val JDK_11 = "https://www.oracle.com/java/technologies/javase/11u-relnotes.html"
        private const val JDK_17 = "https://www.oracle.com/java/technologies/javase/17u-relnotes.html"
        private const val JDK_21 = "https://www.oracle.com/java/technologies/javase/21u-relnotes.html"
        private const val JDK_23 = "https://www.oracle.com/java/technologies/javase/23u-relnotes.html"

        private fun getReleaseInfo(project: String, url: String): List<Release> {
            return if (url == JDK_8) {
                getReleases(project, url, ".col-item")
            } else {
                getReleases(project, url, ".obullets")
            }
        }

        private fun getReleases(project: String, url: String, selector: String): List<Release> {
            return runCatching {
                val doc = Jsoup.connect(url).get()
                doc.select(selector)
                    .flatMap { it.select("li") }
                    .mapNotNull { getRelease(project, it) }
                    .sortedByDescending { it.version }
            }.getOrElse {
                logger.error("Failed to fetch releases for $project: ${it.message}")
                emptyList()
            }
        }

        private fun getRelease(project: String, li: Element): Release? {
            val liText = li.text()
            if (!liText.contains("GA")) return null

            return runCatching {
                val version = liText.split(" \\(".toRegex()).firstOrNull() ?: return null
                val href = li.select("a").firstOrNull()?.attr("href") ?: return null
                val gaUrl = ORACLE_BASE_URL + href
                Release(
                    project = project,
                    version = version,
                    url = gaUrl,
                    tags = listOf(project, "java", "release"),
                    createdDt = LocalDate.now().format(DateUtils.CREATED_FORMATTER),
                )
            }.getOrElse {
                println("Failed to parse release for $project: ${it.message}")
                null
            }
        }

        private data class Release(
            val project: String,
            val version: String,
            val url: String,
            val tags: List<String>,
            val createdDt: String,
        )
    }
}
