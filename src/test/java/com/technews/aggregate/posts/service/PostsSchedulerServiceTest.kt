package com.technews.aggregate.posts.service

import com.technews.aggregate.posts.createPost
import com.technews.aggregate.posts.domain.repository.PostsRepository
import com.technews.common.constant.JavaBlogsSubject
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class PostsSchedulerServiceTest : BehaviorSpec({
    val postsRepository = mockk<PostsRepository>()

    val postsSchedulerService = PostsSchedulerService(postsRepository)

    Given("특정 카테고리의 게시물이 존재하는 경우") {
        val post = listOf(createPost(date = "2024-12-25"))

        every { postsRepository.findByCategoryOrderByDateDescLimitOne(any()) } returns post

        When("가장 마지막으로 수집된 게시을 조회하여") {
            Then("게시일을 확인할 수 있다.") {
                val lastPost = postsSchedulerService.findLastPost(JavaBlogsSubject.INSIDE.value)
                lastPost.date shouldBe "2024-12-25"
            }
        }
    }

    Given("특정 카테고리의 게시물이 존재하지 않는 경우") {
        every { postsRepository.findByCategoryOrderByDateDescLimitOne(any()) } returns emptyList()

        When("기본 엔티티를 리턴하여") {
            Then("기본 게시일을 확인할 수 있다.") {
                val lastPost = postsSchedulerService.findLastPost(JavaBlogsSubject.INSIDE.value)
                lastPost.date shouldBe ""
            }
        }
    }
})
