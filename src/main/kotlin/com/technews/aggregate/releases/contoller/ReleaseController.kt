package com.technews.aggregate.releases.contoller

import com.technews.aggregate.releases.domain.Release
import com.technews.aggregate.releases.service.ReleasesService
import com.technews.common.dto.BasicResponse
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/releases")
class ReleaseController(
    private val releasesService: ReleasesService
) {
    @GetMapping
    fun scrollList(
        @RequestParam(value = "categories", required = false) categories: List<String>,
        @RequestParam(value = "page", required = false, defaultValue = "1") page: Int,
        @RequestParam(value = "size", required = false, defaultValue = "10") size: Int
    ): ResponseEntity<*> {
        val releasePage: Page<Release> = releasesService.findAllRelease(page, size, categories)
        return BasicResponse.ok(releasePage)
    }
}
