package com.technews.crawling.release

import io.kotest.core.spec.style.BehaviorSpec
import mu.KotlinLogging
import org.jsoup.Jsoup

private val logger = KotlinLogging.logger {}

class kafkaReleaseCrawlingTest : BehaviorSpec({
    Given("kafka releases 정보 크롤링") {
        Jsoup.connect(BASE_RELEASES_URL)
            .get()
            .select(".download-version")
            .mapNotNull {
                println("==========")
                val version = it.text()
                println("version :" + version)
                println("https://kafka.apache.org/downloads#$version")
                val select = it.nextElementSibling().select("ul").select("li")
                println(select.get(0).text())
                println(select.get(1).select("a").attr("href"))
                println("https://archive.apache.org/dist/kafka/$version/RELEASE_NOTES.html")

                //IndexOutOfBoundsException
            }
            .toList()
    }
}) {
    companion object {
        private const val BASE_RELEASES_URL = "https://kafka.apache.org/downloads"

        private data class Releases(
            val project: String,
            var version: String,
            val publishDate: String,
            val url: String,
        )
    }
}
