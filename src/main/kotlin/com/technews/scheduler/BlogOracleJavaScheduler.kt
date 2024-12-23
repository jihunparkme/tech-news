package com.technews.scheduler

import com.technews.aggregate.posts.constant.JavaBlogsSubject
import com.technews.aggregate.posts.constant.PostSubjects
import com.technews.aggregate.posts.dto.SavePostRequest
import com.technews.aggregate.posts.service.PostsSchedulerService
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

@Component
class BlogOracleJavaScheduler(
    private val postsSchedulerService: PostsSchedulerService,
) {
    @Scheduled(cron = "0 0 1 * * ?")
    fun runScheduled() {
        searchOracleJavaBlogPosts()
    }

    private fun searchOracleJavaBlogPosts() {
        posts.filter { post ->
            post.let { postsSchedulerService.isNotExistOracleJavaPosts(it.title) }
        }.map { post ->
            post.toSavePostRequest()
        }.forEach { savePostRequest ->
            postsSchedulerService.insertPost(savePostRequest)
        }
    }

    data class OracleJavaBlogPost(
        val subject: String,
        val title: String,
        val url: String,
        val category: String,
        val writer: String,
        val date: String,
        val tags: List<String>,
        val createdDt: String,
    ) {
        fun toSavePostRequest(): SavePostRequest {
            return SavePostRequest(
                subject = subject,
                title = title,
                url = url,
                category = category,
                writer = writer,
                date = date,
                tags = tags,
                createdDt = createdDt
            )
        }
    }

    companion object {
        private const val BLOG_BASE_URL = "https://blogs.oracle.com/java/"
        private val CATEGORIES = listOf("Product & Ecosystem", "Java Technology")
        private val SAVED_POST_TITLE: MutableSet<String> = mutableSetOf()

        data class PostInfo(
            val title: String = "",
            val url: String = "",
            val writer: String = "",
        ) {
            companion object {
                val EMPTY = PostInfo()
            }
        }

        private val posts: List<OracleJavaBlogPost>
            get() = runCatching {
                Jsoup.connect(BLOG_BASE_URL)
                    .header("User-Agent", "PostmanRuntime/7.37.3")
                    .get()
                    .select(".with-category")
                    .filter { categoryElement ->
                        CATEGORIES.contains(categoryElement.select(".rw-ptitle").text())
                    }.flatMap { categoryElement ->
                        getPosts(categoryElement.select(".rw-ptitle").text(), categoryElement)
                    }
            }.getOrElse {
                logger.error("Failed to fetch posts: ${it.message}", it)
                emptyList()
            }

        private fun getPosts(category: String, postElement: Element): List<OracleJavaBlogPost> {
            return postElement.select(".cscroll-item-w1").mapNotNull { element ->
                val postInfo = getPostInfo(element)
                if (SAVED_POST_TITLE.contains(postInfo.title)) null
                else {
                    SAVED_POST_TITLE.add(postInfo.title)
                    OracleJavaBlogPost(
                        subject = PostSubjects.JAVA.value,
                        title = postInfo.title,
                        url = postInfo.url,
                        category = JavaBlogsSubject.ORACLE.value,
                        writer = postInfo.writer,
                        date = LocalDate.now().toString(),
                        tags = listOf(JavaBlogsSubject.ORACLE.value, category),
                        createdDt = LocalDate.now().toString()
                    )
                }
            }
        }


        private fun getPostInfo(element: Element): PostInfo {
            return runCatching {
                val blogTile = element.select(".blogtile-w2")
                val post = blogTile.select("a")
                PostInfo(
                    title = post.getOrNull(2)?.text().orEmpty(),
                    url = BLOG_BASE_URL + post.getOrNull(2)?.attr("href").orEmpty(),
                    writer = post.getOrNull(3)?.text().orEmpty()
                )
            }.getOrDefault(PostInfo.EMPTY)
        }
    }
}