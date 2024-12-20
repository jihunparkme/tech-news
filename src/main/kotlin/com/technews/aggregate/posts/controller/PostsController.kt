package com.technews.aggregate.posts.controller

import com.technews.aggregate.posts.constant.PostSubjects
import com.technews.aggregate.posts.service.PostsService
import com.technews.common.dto.BasicResponse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostsController(
    private val postsService: PostsService
) {
    @GetMapping("/spring")
    fun springScroll(
        @RequestParam(value = "categories", required = false) categories: List<String>,
        @RequestParam(value = "page", required = false, defaultValue = "1") page: Int,
        @RequestParam(value = "size", required = false, defaultValue = "10") size: Int
    ): ResponseEntity<*> {
        val pageable = PageRequest.of(
            page, size,
            Sort.by("createdDt").descending().and(Sort.by("date").descending())
        )
        val releasePage = postsService.findAllRelease(PostSubjects.SPRING, pageable, categories)
        return BasicResponse.ok(releasePage)
    }

    @GetMapping("/java")
    fun javaScroll(
        @RequestParam(value = "categories", required = false) categories: List<String>,
        @RequestParam(value = "page", required = false, defaultValue = "1") page: Int,
        @RequestParam(value = "size", required = false, defaultValue = "10") size: Int
    ): ResponseEntity<*> {
        val pageable = PageRequest.of(
            page, size,
            Sort.by("createdDt").descending().and(Sort.by("date").descending())
        )
        val releasePage = postsService.findAllRelease(PostSubjects.JAVA, pageable, categories)
        return BasicResponse.ok(releasePage)
    }
}
