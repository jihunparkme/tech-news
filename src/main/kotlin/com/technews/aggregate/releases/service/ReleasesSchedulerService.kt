package com.technews.aggregate.releases.service

import com.technews.aggregate.releases.domain.Release
import com.technews.aggregate.releases.domain.repository.ReleasesRepository
import com.technews.aggregate.releases.dto.SaveReleaseRequest
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {}

@Service
class ReleasesSchedulerService(
    private val releasesRepository: ReleasesRepository,
) {
    @Transactional
    fun insertRelease(saveReleaseRequest: SaveReleaseRequest) {
        runCatching {
            releasesRepository.save<Release>(saveReleaseRequest.toRelease())
            logger.info("add new release version. ${saveReleaseRequest.version}")
        }.onFailure { e ->
            logger.error("ReleasesSchedulerService.insertRelease exception", e)
        }
    }

    @Transactional(readOnly = true)
    fun findLatestRelease(project: String): Release =
        releasesRepository.findByProjectOrderByVersionDescLimitOne(project).firstOrNull() ?: Release()
}
