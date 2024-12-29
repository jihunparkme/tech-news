package com.technews.scheduler

import com.technews.aggregate.posts.constant.JavaBlogsSubject
import com.technews.aggregate.posts.constant.PostSubjects
import com.technews.aggregate.posts.dto.SavePostRequest
import com.technews.aggregate.posts.service.PostsSchedulerService
import com.technews.scheduler.dto.OracleJavaBlogPostInfo
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

@Deprecated("HTTP error fetching URL. Status=403, URL=[https://blogs.oracle.com/java/]")
@Component
class BlogOracleJavaScheduler(
    private val postsSchedulerService: PostsSchedulerService,
) {
    @Scheduled(cron = "0 0 1 * * ?")
    fun runScheduled() {
        searchOracleJavaBlogPosts()
    }

    private fun searchOracleJavaBlogPosts() {
        posts.filter { post -> postsSchedulerService.isNotExistOracleJavaPosts(post.title) }
            .forEach { postsSchedulerService.insertPost(it) }
    }

    companion object {
        private const val BLOG_BASE_URL = "https://blogs.oracle.com/java/"
        private val CATEGORIES = listOf("Product & Ecosystem", "Java Technology")
        private val SAVED_POST_TITLE: MutableSet<String> = mutableSetOf()

        private val posts: List<SavePostRequest>
            get() = runCatching {
                Jsoup.connect(BLOG_BASE_URL)
                    .header("User-Agent", "PostmanRuntime/7.37.3")
                    .get()
                    .select(".with-category")
                    .filter { category -> category.hasCategory() }
                    .flatMap { category -> category.getPostsForCategory() }
            }.getOrElse {
                logger.error("Failed to fetch posts: ${it.message}", it)
                emptyList()
            }

        private fun getPosts(category: String, postElement: Element): List<SavePostRequest> {
            return postElement.select(".cscroll-item-w1").mapNotNull { element ->
                val postInfo = getPostInfo(element)
                if (SAVED_POST_TITLE.contains(postInfo.title)) {
                    null
                } else {
                    SAVED_POST_TITLE.add(postInfo.title)
                    SavePostRequest(
                        subject = PostSubjects.JAVA.value,
                        title = postInfo.title,
                        url = postInfo.url,
                        category = JavaBlogsSubject.ORACLE.value,
                        writer = postInfo.writer,
                        date = LocalDate.now().toString(),
                        tags = listOf(JavaBlogsSubject.ORACLE.value, category),
                        createdDt = LocalDate.now().toString(),
                    )
                }
            }
        }

        private fun getPostInfo(element: Element): OracleJavaBlogPostInfo {
            return runCatching {
                val blogTile = element.select(".blogtile-w2")
                val post = blogTile.select("a")
                OracleJavaBlogPostInfo(
                    title = post.getOrNull(2)?.text().orEmpty(),
                    url = BLOG_BASE_URL + post.getOrNull(2)?.attr("href").orEmpty(),
                    writer = post.getOrNull(3)?.text().orEmpty(),
                )
            }.getOrDefault(OracleJavaBlogPostInfo())
        }

        private fun Element.hasCategory(): Boolean {
            return CATEGORIES.contains(this.select(".rw-ptitle").text())
        }

        private fun Element.getPostsForCategory(): List<SavePostRequest> {
            val categoryName = this.select(".rw-ptitle").text()
            return getPosts(categoryName, this)
        }
    }
}
