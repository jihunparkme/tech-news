package com.technews.crawling.blog

import com.technews.aggregate.posts.dto.SavePostRequest
import com.technews.common.dto.Project
import com.technews.common.util.DateUtils
import io.kotest.core.spec.style.BehaviorSpec
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

class KafkaBlogCrawlingTest : BehaviorSpec({
    Given("crawling a kafka blog post") {
        Jsoup.connect(BASE_BLOG_URL)
            .get()
            .select("article")
            .mapNotNull { it.toPostOrNull() }
            .forEach { println(it) }
    }
}) {
    companion object {
        private const val BASE_BLOG_URL = "https://kafka.apache.org/blog"

        private fun Element.toPostOrNull(): SavePostRequest? = runCatching {
            val postInfo = childNodes().filterIsInstance<TextNode>().get(1).text().trim().split(" - ")

            SavePostRequest(
                subject = Project.KAFKA.value,
                title = select("h2").text(),
                url = BASE_BLOG_URL + select("h2").select("a").attr("href"),
                category = Project.KAFKA.value,
                writer = postInfo[1].replace(" (", ""),
                date = DateUtils.parseEnglishDateFormat(postInfo[0]),
                tags = listOf(Project.KAFKA.value, "Blog"),
                createdDt = LocalDate.now().toString(),
            )
        }.getOrElse {
            logger.error(it) { "Failed to parse post" }
            null
        }
    }
}
