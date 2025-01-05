package com.technews.scheduler.blog

import com.technews.aggregate.posts.constant.JavaBlogsSubject
import com.technews.aggregate.posts.dto.SavePostRequest
import com.technews.aggregate.posts.service.PostsSchedulerService
import com.technews.common.dto.Project
import com.technews.common.util.DateUtils
import com.technews.scheduler.dto.JavaInsidePostInfo
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

@Component
class BlogJavaInsideScheduler(
    private val postsSchedulerService: PostsSchedulerService,
) {
    @Scheduled(cron = "0 0 1 * * ?")
    fun runSchedule() {
        searchJavaBlogPosts(JavaBlogsSubject.INSIDE)
    }

    private fun searchJavaBlogPosts(subject: JavaBlogsSubject) {
        val lastPost = postsSchedulerService.findLastPost(subject.value)
        posts.filter { it.isNotJobOpeningPost() }
            .filter { it.isLatestDatePost(lastPost.date) }
            .forEach { postsSchedulerService.insertPost(it) }
    }

    companion object {
        private const val BLOG_URL = "https://inside.java"

        private val posts: List<SavePostRequest>
            get() = try {
                Jsoup.connect(BLOG_URL).get()
                    .select(".post")
                    .map { getPost(it) }
            } catch (e: Exception) {
                logger.error("Failed to fetch posts: ${e.message}", e)
                emptyList()
            }

        private fun getPost(postElement: Element): SavePostRequest {
            return SavePostRequest(
                subject = Project.JAVA.value,
                category = JavaBlogsSubject.INSIDE.value,
                title = getTitle(postElement),
                url = getUrl(postElement),
                writer = getPostInfo(postElement).writer,
                date = DateUtils.parseEnglishDateFormat(getPostInfo(postElement).date),
                tags = getTags(postElement) + listOf("Java"),
                createdDt = LocalDate.now().format(DateUtils.CREATED_FORMATTER),
            )
        }

        private fun getTitle(postElement: Element): String =
            runCatching { postElement.select(".post-title").text() }
                .getOrDefault("Untitle")

        private fun getUrl(postElement: Element): String =
            runCatching {
                val url = postElement.select("a").first()?.attr("href").orEmpty()
                if (url.startsWith("http")) url else "$BLOG_URL$url"
            }.getOrDefault(BLOG_URL)

        private fun getPostInfo(postElement: Element): JavaInsidePostInfo {
            val info = postElement.select(".post-info").text()
            return runCatching {
                val split = info.split(" on ")
                if (split.size == 2) {
                    JavaInsidePostInfo(split[0], split[1])
                } else {
                    JavaInsidePostInfo(
                        writer = "",
                        date = split.firstOrNull() ?: "",
                    )
                }
            }.getOrDefault(JavaInsidePostInfo())
        }

        private fun getTags(postElement: Element): List<String> =
            postElement.select("span#post-tags .tag-small").map { it.text() }
    }
}
