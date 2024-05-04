package com.technews.aggregate.posts.spring.controller;

import com.technews.aggregate.posts.domain.Post;
import com.technews.aggregate.posts.spring.service.SpringBlogsService;
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
public class SpringBlogsController {

    private final SpringBlogsService springBlogsService;

    @GetMapping("/list")
    public ResponseEntity scrollList(
            @RequestParam(value = "categories", required = false) final List<String> categories,
            @RequestParam(value = "page", required = false, defaultValue = "1") final int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") final int size) {

        final PageRequest pageable = PageRequest.of(
                page, size,
                Sort.by("createdDt").descending().and(Sort.by("version").descending()));
        final Page<Post> releasePage = springBlogsService.findAllRelease(page, size, categories);
        return BasicResponse.ok(releasePage);
    }
}
