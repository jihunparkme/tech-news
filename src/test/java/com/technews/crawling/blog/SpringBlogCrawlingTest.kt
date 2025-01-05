package com.technews.crawling.blog

import io.kotest.core.spec.style.BehaviorSpec
import mu.KotlinLogging
import org.jsoup.Jsoup

private val logger = KotlinLogging.logger {}

class SpringBlogCrawlingTest : BehaviorSpec({
    Given("Spring BLog 정보 크롤링") {
        val categories = listOf(ENGINEERING, RELEASES, NEWS)

        categories.forEach { category ->
            println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> $category")
            val posts = fetchPostsByCategory(category)
            posts.forEach { println(it) }
        }
    }
}) {
    companion object {
        private const val CATEGORY_URL = "https://spring.io/blog/category/"
        private const val BLOG_URL = "https://spring.io"
        private const val ENGINEERING = "engineering"
        private const val RELEASES = "releases"
        private const val NEWS = "news"

        private fun fetchPostsByCategory(category: String): List<Post> {
            return runCatching {
                Jsoup.connect("$CATEGORY_URL$category").get()
                    .select(".blog-post")
                    .mapNotNull { element ->
                        val title = element.select(".has-text-weight-medium").text().ifEmpty { null }
                        val meta = parseMeta(element.select(".meta").text())
                        val url =
                            element.select(".button").attr("href").takeIf { it.isNotEmpty() }?.let { BLOG_URL + it }

                        if (title != null && meta != null && url != null) {
                            Post(title, meta, url)
                        } else {
                            null
                        }
                    }
            }.getOrElse {
                logger.error("Failed to fetch posts for category: $category, error: ${it.message}", it)
                emptyList()
            }
        }

        private fun parseMeta(metaText: String): Meta? {
            val metaParts = metaText.split("|").map { it.trim() }
            return if (metaParts.size >= 3) {
                Meta(metaParts[0], metaParts[1], metaParts[2])
            } else {
                logger.warn("Invalid meta format: $metaText")
                null
            }
        }

        private data class Post(
            val title: String,
            val meta: Meta,
            val url: String,
        )

        private data class Meta(
            val category: String,
            val writer: String,
            val date: String,
        )
    }
}
