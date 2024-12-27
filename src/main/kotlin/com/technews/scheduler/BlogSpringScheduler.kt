package com.technews.scheduler

import com.technews.aggregate.posts.constant.PostSubjects
import com.technews.aggregate.posts.constant.SpringBlogsSubject
import com.technews.aggregate.posts.dto.SavePostRequest
import com.technews.aggregate.posts.service.PostsSchedulerService
import com.technews.common.util.DateUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class BlogSpringScheduler(
    private val postsSchedulerService: PostsSchedulerService,
) {

    @Scheduled(cron = "0 0 1 * * ?")
    fun runScheduler() {
        searchSpringBlogPosts(SpringBlogsSubject.ENGINEERING)
        searchSpringBlogPosts(SpringBlogsSubject.RELEASES)
        searchSpringBlogPosts(SpringBlogsSubject.NEWS)
    }

    private fun searchSpringBlogPosts(subject: SpringBlogsSubject) {
        val lastPost = postsSchedulerService.findLastPost(subject.value)
        val posts = getPostInfo(subject.value)
        posts.filter { it.isLatestDatePost(lastPost.date) }
            .forEach { postsSchedulerService.insertPost(it) }
    }

    companion object {
        private const val CATEGORY_URL = "https://spring.io/blog/category/"
        private const val BLOG_URL = "https://spring.io"

        data class Meta(
            val category: String = "",
            val writer: String = "",
            val date: String = "",
        ) {
            companion object {
                val EMPTY = Meta()
            }
        }

        private fun getPostInfo(category: String): List<SavePostRequest> = try {
            val doc = Jsoup.connect("$CATEGORY_URL$category").get()
            val elements = doc.select(".blog-post")

            elements.map { element ->
                SavePostRequest(
                    subject = PostSubjects.SPRING.value,
                    title = getTitle(element),
                    url = getPostUrl(element),
                    category = category,
                    writer = getMeta(element).writer,
                    date = getMeta(element).date,
                    tags = listOf(PostSubjects.SPRING.value, category),
                    createdDt = LocalDate.now().format(DateUtils.CREATED_FORMATTER),
                )
            }
        } catch (e: Exception) {
            emptyList()
        }

        private fun getTitle(element: Element): String =
            runCatching { element.select(".has-text-weight-medium").text() }.getOrDefault("")

        private fun getPostUrl(element: Element): String =
            runCatching {
                val url = element.select(".button").attr("href")
                "$BLOG_URL$url"
            }.getOrDefault("")

        private fun getMeta(element: Element): Meta {
            val metaText = element.select(".meta").text()
            val metas = metaText.split("|").map { it.trim() }
            if (metas.size < 3) return Meta.EMPTY

            return runCatching {
                Meta(
                    category = metas[0],
                    writer = metas[1],
                    date = DateUtils.getFormattedDate(metas[2]),
                )
            }.getOrDefault(Meta.EMPTY)
        }
    }
}
