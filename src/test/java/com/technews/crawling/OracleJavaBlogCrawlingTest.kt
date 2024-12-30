package com.technews.crawling

import io.kotest.core.spec.style.BehaviorSpec
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

class OracleJavaBlogCrawlingTest : BehaviorSpec({
    Given("Oracle Java Blog 정보 크롤링") {
        val posts = fetchPosts()
        println("posts.size = ${posts.size}")
        posts.forEach { println(it) }
    }
}) {
    companion object {
        private const val BLOG_BASE_URL = "https://blogs.oracle.com/java/"
        private val CATEGORIES = listOf("Product & Ecosystem", "Java Technology")
        private val SAVED_POST_TITLE: MutableSet<String> = mutableSetOf()

        private fun fetchPosts(): List<OracleJavaBlogPost> {
            return runCatching {
                Jsoup.connect(BLOG_BASE_URL)
                    .header("User-Agent", "PostmanRuntime/7.37.3")
                    .get()
                    .select(".with-category")
                    .flatMap { categoryElement ->
                        val category = categoryElement.select(".rw-ptitle").text()
                        if (category in CATEGORIES) {
                            getPosts(category, categoryElement)
                        } else {
                            emptyList()
                        }
                    }
            }.getOrElse {
                logger.error("Jsoup parsing exception. Message: ${it.message}", it)
                emptyList()
            }
        }

        private fun getPosts(category: String, postElement: Element): List<OracleJavaBlogPost> {
            return postElement.select(".cscroll-item-w1")
                .mapNotNull { element ->
                    val postInfo = getPostInfo(element)
                    if (SAVED_POST_TITLE.add(postInfo.title)) {
                        OracleJavaBlogPost(
                            subject = "java",
                            title = postInfo.title,
                            url = postInfo.url,
                            category = "oracle",
                            writer = postInfo.writer,
                            date = LocalDate.now().toString(),
                            tags = listOf("oracle", category),
                            createdDt = LocalDate.now().toString()
                        )
                    } else {
                        null
                    }
                }
        }

        private fun getPostInfo(element: Element): PostInfo {
            return runCatching {
                val blogTile = element.select(".blogtile-w2")
                val postLinks = blogTile.select("a")
                PostInfo(
                    title = postLinks.getOrNull(2)?.text().orEmpty(),
                    url = BLOG_BASE_URL + postLinks.getOrNull(2)?.attr("href").orEmpty(),
                    writer = postLinks.getOrNull(3)?.text().orEmpty()
                )
            }.getOrDefault(PostInfo())
        }

        private data class OracleJavaBlogPost(
            val subject: String = "",
            val title: String = "",
            val url: String = "",
            val category: String = "",
            val writer: String = "",
            val date: String = "",
            var tags: List<String> = emptyList(),
            val createdDt: String = "",
        )

        private data class PostInfo(
            val title: String = "",
            val url: String = "",
            val writer: String = "",
        )
    }
}
