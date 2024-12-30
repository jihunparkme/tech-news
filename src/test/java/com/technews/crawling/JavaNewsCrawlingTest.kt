package com.technews.crawling

import io.kotest.core.spec.style.BehaviorSpec
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class JavaNewsCrawlingTest : BehaviorSpec({
    Given("Java News 정보 크롤링") {
        val doc = Jsoup.connect(BLOG_URL).get()
        val posts = doc.select(".post").mapNotNull { getPost(it) }
        posts.forEach{ println(it) }
    }
}) {
    companion object {
        private const val BLOG_URL = "https://inside.java"

        private fun getPost(postElement: Element): Post? {
            val title = postElement.select(".post-title").text()
            val url = buildUrl(postElement.select("a").first()?.attr("href").orEmpty())
            val postInfo = getPostInfo(postElement)
            val tags = postElement.select("span#post-tags .tag-small").map { it.text() }

            return if (title.isNotEmpty() && url.isNotEmpty() && postInfo != PostInfo()) {
                Post(title, Meta(postInfo.writer, postInfo.date, tags), url)
            } else {
                null
            }
        }

        private fun getPostInfo(postElement: Element): PostInfo {
            val info = postElement.select(".post-info").text()
            val split = info.split(" on ").map { it.trim() }

            return when (split.size) {
                2 -> PostInfo(split[0], split[1])
                1 -> PostInfo(writer = "", date = split[0])
                else -> PostInfo()
            }
        }

        private fun buildUrl(url: String): String =
            if (url.startsWith("http")) url else BLOG_URL + url

        private data class Post(
            val title: String = "",
            val meta: Meta = Meta(),
            val url: String = "",
        )

        private data class Meta(
            val writer: String = "",
            val date: String = "",
            val tags: List<String> = emptyList(),
        )

        private data class PostInfo(
            val writer: String = "",
            val date: String = "",
        )
    }
}
