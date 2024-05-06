package com.technews.aggregate.posts.controller;

import com.technews.aggregate.posts.constant.PostSubjects;
import com.technews.aggregate.posts.domain.Post;
import com.technews.aggregate.posts.service.PostsService;
import com.technews.common.dto.BasicResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;

    @GetMapping("/spring")
    public ResponseEntity springScroll(
            @RequestParam(value = "categories", required = false) final List<String> categories,
            @RequestParam(value = "page", required = false, defaultValue = "1") final int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") final int size) {

        final PageRequest pageable = PageRequest.of(page, size,
                Sort.by("createdDt").descending().and(Sort.by("date").descending()));
        final Page<Post> releasePage = postsService.findAllRelease(PostSubjects.SPRING, pageable, categories);
        return BasicResponse.ok(releasePage);
    }

    @GetMapping("/java")
    public ResponseEntity javaScroll(
            @RequestParam(value = "categories", required = false) final List<String> categories,
            @RequestParam(value = "page", required = false, defaultValue = "1") final int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") final int size) {

        final PageRequest pageable = PageRequest.of(page, size,
                Sort.by("createdDt").descending().and(Sort.by("date").descending()));
        final Page<Post> releasePage = postsService.findAllRelease(PostSubjects.JAVA, pageable, categories);
        return BasicResponse.ok(releasePage);
    }
}
