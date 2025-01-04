package com.technews.crawling.blog

import io.kotest.core.spec.style.BehaviorSpec
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.TextNode

private val logger = KotlinLogging.logger {}

class KafkaBlogCrawlingTest : BehaviorSpec({
    Given("crawling a kafka blog post") {
        Jsoup.connect(BASE_BLOG_URL)
            .get()
            .select("article")
            .map {
                println("title : " + it.select("h2").text())
                println("url : " + BASE_BLOG_URL + it.select("h2").select("a").attr("href"))
                val postInfo = it.childNodes().filterIsInstance<TextNode>().get(1).text().trim()
                println("writer : " + postInfo.split(" - ")[0])
                println("date : " + postInfo.split(" - ")[1].replace(" (", ""))
                // TODO: date -> yyyy-mm-dd
                println()
            }
    }
}) {
    companion object {
        private const val BASE_BLOG_URL = "https://kafka.apache.org/blog"
    }
}
