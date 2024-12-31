package com.technews.scheduler

import com.technews.aggregate.posts.constant.PostSubjects
import com.technews.aggregate.posts.domain.Post
import com.technews.aggregate.posts.domain.repository.PostsRepository
import com.technews.aggregate.releases.domain.Release
import com.technews.aggregate.releases.domain.repository.ReleasesRepository
import com.technews.aggregate.subscribe.dto.SubscriberMailContents
import com.technews.aggregate.subscribe.service.SubscribeService
import com.technews.common.config.event.Events
import com.technews.common.constant.JdkVersion
import com.technews.common.constant.SpringRepository
import com.technews.common.event.dto.SendMailEvent
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class SubscribeMailScheduler(
    private val releasesRepository: ReleasesRepository,
    private val postsRepository: PostsRepository,
    private val subscribeService: SubscribeService,
) {
    @Scheduled(cron = "0 0 9 * * *")
    fun runSchedule() {
        logger.info("[SubscribeMailScheduler] Start...")
        val releases = releasesRepository.findBySharedFalse()
        val posts = postsRepository.findBySharedFalse()
        if (releases.isEmpty() && posts.isEmpty()) {
            logger.info("[SubscribeMailScheduler] There are no news")
            return
        }

        val subscriberMailContents = createSubscriberMailContents(releases, posts)
        val subscriber = subscribeService.findSubscriberEmail()
        Events.raise(
            SendMailEvent(
                subject = SUBJECT,
                contents = subscriberMailContents,
                addressList = subscriber,
            ),
        )

        updateShared(releases, posts)
    }

    private fun createSubscriberMailContents(
        releases: List<Release>,
        posts: List<Post>,
    ): SubscriberMailContents {
        val releaseOfSpring = getReleaseOfSpring(releases)
        val releaseOfJava = getReleaseOfJava(releases)
        val postOfSpring = getPostOfSpring(posts)
        val postOfJava = getPostOfJava(posts)

        return SubscriberMailContents(
            springReleases = releaseOfSpring,
            javaReleases = releaseOfJava,
            springPosts = postOfSpring,
            javaPosts = postOfJava,
        )
    }

    private fun getReleaseOfSpring(releases: List<Release>): List<Release> =
        releases.filter { SpringRepository.list().contains(it.project) }

    private fun getReleaseOfJava(releases: List<Release>): List<Release> =
        releases.filter { JdkVersion.list().contains(it.project) }

    private fun getPostOfSpring(posts: List<Post>): List<Post> =
        posts.filter { PostSubjects.SPRING.value == it.subject }

    private fun getPostOfJava(posts: List<Post>): List<Post> =
        posts.filter { PostSubjects.JAVA.value == it.subject }

    private fun updateShared(
        releases: List<Release>,
        posts: List<Post>,
    ) {
        releases.forEach { it.updateShare() }
        posts.forEach { it.updateShare() }
    }

    private fun Release.updateShare() {
        this.share()
        releasesRepository.save(this)
    }

    private fun Post.updateShare() {
        this.share()
        postsRepository.save(this)
    }

    companion object {
        private const val SUBJECT = "Java & Spring News"
    }
}
