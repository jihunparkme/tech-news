package com.technews.crawling

import com.technews.common.util.DateUtils
import io.kotest.core.spec.style.BehaviorSpec
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

private val logger = KotlinLogging.logger {}

class JavaOracleBlogRssCrawlingTest : BehaviorSpec({
    Given("java oracle blog rss 크롤링") {
        fetchPost().forEach { println(it) }
    }
}) {
    companion object {
        private const val BLOG_RSS_URL = "https://blogs.oracle.com/java/rss"

        private fun fetchPost(): List<JavaOracleBlogPost> =
            runCatching {
                Jsoup.connect(BLOG_RSS_URL)
                    .header("User-Agent", "PostmanRuntime/7.37.3")
                    .header(
                        "Cookie",
                        "AK_NETWORKTYPE=ESSL; akaalb_BLOGS_PROD_OCI=1735649402~op=Blogs_OCE:oceSitesProdOrigin|~rv=48~m=oceSitesProdOrigin:0|~os=4ea2ebfe4dd25685ddb20c6059d0cd0a~id=bc25bd30e64280e2785e4dcf77d70f51"
                    )
                    .get()
                    .select("item")
                    .mapNotNull { it.toPostOrNull() }
                    .toList()
            }.getOrElse {
                logger.error(it) { "RSS parsing exception: ${it.message}" }
                emptyList()
            }

        private data class JavaOracleBlogPost(
            val title: String,
            val description: String,
            val link: String,
            val publishDate: String,
        )

        private fun Element.toPostOrNull(): JavaOracleBlogPost? =
            runCatching {
                JavaOracleBlogPost(
                    title = select("title").text(),
                    description = select("description").text(),
                    link = select("link").text(),
                    publishDate = DateUtils.gmtToSeoul(select("pubDate").text()),
                )
            }.getOrElse {
                logger.error(it) { "Failed to parse post: ${it.message}" }
                null
            }
    }
}


