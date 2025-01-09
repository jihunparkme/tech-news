package com.technews.crawling.release

import io.kotest.core.spec.style.BehaviorSpec
import mu.KotlinLogging
import org.jsoup.Jsoup

private val logger = KotlinLogging.logger {}

class KotlinReleasesCrawlingTest : BehaviorSpec({
    Given("kafka releases 정보 크롤링") {
        Jsoup.connect(BASE_RELEASES_URL)
            .get()
            .select("section")
            .map {
                println("title : " + it.select("relative-time").attr("datetime"))
                println("version : " + it.select(".Box-body").select("a").select(".Link--primary").text())
                println("label : " + it.select(".Box-body").select("flex-column"))
                println("url : " + GITHUB_URL + it.select(".Box-body").select("a").select(".Link--primary").attr("href"))
                println()
                println()
            }
            .forEach { println(it) }
    }
}) {
    companion object {
        private const val BASE_RELEASES_URL = "https://github.com/JetBrains/kotlin/releases"
        private const val GITHUB_URL = "https://github.com/"
    }
}
