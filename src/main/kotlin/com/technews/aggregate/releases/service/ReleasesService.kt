package com.technews.aggregate.releases.service

import com.technews.aggregate.releases.domain.repository.ReleasesRepository
import com.technews.aggregate.releases.dto.ReleaseResponse
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger { }

@Service
class ReleasesService(
    private val releasesRepository: ReleasesRepository,
) {
    @Transactional(readOnly = true)
    fun findAllRelease(
        pageable: PageRequest,
        categories: List<String>? = emptyList(),
    ): Page<ReleaseResponse> {
        val releasePage = if (categories.isNullOrEmpty()) {
            releasesRepository.findAll(pageable)
        } else {
            releasesRepository.findByProjectIn(categories, pageable)
        }

        return releasePage.map { release ->
            ReleaseResponse(
                id = release.id ?: "",
                project = release.project,
                version = release.version,
                date = release.date,
                url = release.url,
                tags = release.tags,
                shared = release.shared,
                createdDt = release.createdDt,
            )
        }
    }
}
