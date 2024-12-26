package com.technews.aggregate.releases.contoller

import com.technews.aggregate.releases.domain.Release
import com.technews.aggregate.releases.dto.ReleaseResponse
import com.technews.aggregate.releases.service.ReleasesService
import com.technews.common.dto.BasicResponse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/releases")
class ReleaseController(
    private val releasesService: ReleasesService,
) {
    @GetMapping
    fun scrollList(
        @RequestParam(value = "categories", required = false) categories: List<String>?,
        @RequestParam(value = "page", required = false, defaultValue = "1") page: Int,
        @RequestParam(value = "size", required = false, defaultValue = "10") size: Int,
        assembler: PagedResourcesAssembler<ReleaseResponse>,
    ): ResponseEntity<*> {
        val pageable: PageRequest = PageRequest.of(
            page, size,
            Sort.by("createdDt").descending().and(Sort.by("version").descending())
        )
        val releasePage = releasesService.findAllRelease(pageable, categories)
        return BasicResponse.ok(assembler.toModel(releasePage))
    }
}
