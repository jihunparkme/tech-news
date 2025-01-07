package com.technews.scheduler.blog

import com.technews.aggregate.posts.dto.SavePostRequest
import com.technews.aggregate.posts.service.PostsSchedulerService
import com.technews.common.constant.Project
import com.technews.common.util.DateUtils
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

@Component
class BlogKafkaScheduler(
    private val postsSchedulerService: PostsSchedulerService,
) {
    @Scheduled(cron = " 0 0 1 * * ?")
    fun runSchedule() {
        searchKafkaBlogPosts()
    }

    private fun searchKafkaBlogPosts() {
        val lastPost = postsSchedulerService.findLastPost(Project.KAFKA.value)
        getPost().filter { it.isLatestDatePost(lastPost.date) }
            .forEach { postsSchedulerService.insertPost(it) }
    }

    companion object {
        private const val BASE_BLOG_URL = "https://kafka.apache.org/blog"

        private fun getPost(): List<SavePostRequest> =
            Jsoup.connect(BASE_BLOG_URL)
                .get()
                .select("article")
                .mapNotNull { it.toPostOrNull() }

        private fun Element.toPostOrNull(): SavePostRequest? = runCatching {
            val postInfo = childNodes().filterIsInstance<TextNode>()[1].text().trim().split(" - ")

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
