package com.technews.aggregate.releases.contoller;

import com.technews.aggregate.releases.springframework.domain.Release;
import com.technews.aggregate.releases.springframework.dto.ReleaseResponse;
import com.technews.aggregate.releases.springframework.service.ReleasesService;
import com.technews.common.dto.BasicResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/releases")
@RequiredArgsConstructor
public class ReleaseController {

    private final ReleasesService releasesService;

    @GetMapping
    public ResponseEntity scrollList(
            @RequestParam(value = "categories", required = false) final List<String> categories,
            @RequestParam(value = "page", required = false, defaultValue = "1") final int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") final int size) {

        final List<Release> releases = releasesService.findAllRelease(page, size, categories);
        final List<ReleaseResponse> result = releases.stream()
                .map(release -> ReleaseResponse.of(release))
                .collect(Collectors.toList());

        return BasicResponse.ok(result);
    }
}
