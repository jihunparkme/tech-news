package com.technews.scheduler.blog

import com.technews.aggregate.posts.constant.JavaBlogsSubject
import com.technews.aggregate.posts.dto.SavePostRequest
import com.technews.aggregate.posts.service.PostsSchedulerService
import com.technews.common.dto.Project
import com.technews.common.util.DateUtils
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

@Component
class BlogOracleJavaRssScheduler(
    private val postsSchedulerService: PostsSchedulerService,
) {
    @Scheduled(cron = "0 0 1 * * ?")
    fun runSchedule() {
        searchOracleJavaBlogPosts()
    }

    private fun searchOracleJavaBlogPosts() {
        val lastPost = postsSchedulerService.findLastPost(JavaBlogsSubject.ORACLE.value)
        fetchPost().filter { it.isLatestDatePost(lastPost.date) }
            .forEach { postsSchedulerService.insertPost(it) }
    }

    companion object {
        private const val BLOG_RSS_URL = "https://blogs.oracle.com/java/rss"

        private fun fetchPost(): List<SavePostRequest> =
            runCatching {
                Jsoup.connect(BLOG_RSS_URL)
                    .header("User-Agent", "PostmanRuntime/7.37.3")
                    .header(
                        "Cookie",
                        "AK_NETWORKTYPE=ESSL; akaalb_BLOGS_PROD_OCI=1735649402~op=Blogs_OCE:oceSitesProdOrigin|~rv=48~m=oceSitesProdOrigin:0|~os=4ea2ebfe4dd25685ddb20c6059d0cd0a~id=bc25bd30e64280e2785e4dcf77d70f51",
                    )
                    .get()
                    .select("item")
                    .mapNotNull { it.toPostOrNull() }
                    .toList()
            }.getOrElse {
                logger.error(it) { "RSS parsing exception: ${it.message}" }
                emptyList()
            }

        private fun Element.toPostOrNull(): SavePostRequest? =
            runCatching {
                SavePostRequest(
                    subject = Project.JAVA.value,
                    title = select("title").text(),
                    url = select("link").text(),
                    category = JavaBlogsSubject.ORACLE.value,
                    date = DateUtils.parseGreenwichToSeoul(select("pubDate").text()),
                    tags = listOf(JavaBlogsSubject.ORACLE.value),
                    createdDt = LocalDate.now().toString(),
                )
            }.getOrElse {
                logger.error(it) { "Failed to parse post: ${it.message}" }
                null
            }
    }
}
