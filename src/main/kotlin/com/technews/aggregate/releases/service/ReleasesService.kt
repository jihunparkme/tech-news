package com.technews.aggregate.releases.service

import com.technews.aggregate.releases.domain.Release
import com.technews.aggregate.releases.domain.repository.ReleasesRepository
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger { }

@Service
class ReleasesService(
    private val releasesRepository: ReleasesRepository,
) {
    @Transactional(readOnly = true)
    fun findAllRelease(
        page: Int,
        size: Int,
        categories: List<String>,
    ): Page<Release> {
        val pageable: PageRequest = PageRequest.of(
            page, size,
            Sort.by("createdDt").descending().and(Sort.by("version").descending())
        )

        return if (categories.isEmpty()) {
            releasesRepository.findAll(pageable)
        } else {
            releasesRepository.findByProjectIn(categories, pageable)
        }
    }
}
