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
import com.technews.common.util.MailTemplateUtils
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
    fun runScheduler() {
        logger.info("[SubscribeMailScheduler] Start...")
        val releases = releasesRepository.findBySharedFalse()
        val posts = postsRepository.findBySharedFalse()
        if (releases.isEmpty() && posts.isEmpty()) {
            logger.info("[SubscribeMailScheduler] There are no news")
            return
        }

        val subscriberMailContents = getSubscriberMailContents(releases, posts)
        val subscriber: List<String> = subscribeService.findSubscriberEmail()
        Events.raise(
            SendMailEvent(
                subject = SUBJECT,
                contents = MailTemplateUtils.generateContents(subscriberMailContents),
                addressList = subscriber,
            ),
        )

        updateShared(releases, posts)
    }

    private fun getSubscriberMailContents(
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

    private fun getReleaseOfSpring(releases: List<Release>): List<Release> {
        return releases.filter { SpringRepository.list().contains(it.project) }
    }

    private fun getReleaseOfJava(releases: List<Release>): List<Release> {
        return releases.filter { JdkVersion.list().contains(it.project) }
    }

    private fun getPostOfSpring(posts: List<Post>): List<Post> {
        return posts.filter { PostSubjects.SPRING.value == it.subject }
    }

    private fun getPostOfJava(posts: List<Post>): List<Post> {
        return posts.filter { PostSubjects.JAVA.value == it.subject }
    }

    private fun updateShared(
        releases: List<Release>,
        posts: List<Post>,
    ) {
        releases.forEach {
            it.share()
            releasesRepository.save(it)
        }
        posts.forEach {
            it.share()
            postsRepository.save(it)
        }
    }

    companion object {
        private const val SUBJECT = "Java & Spring News"
    }
}
