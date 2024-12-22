package com.technews.aggregate.releases.dto

import com.technews.aggregate.releases.domain.Release

data class ReleaseResponse(
    val project: String,
    val version: String,
    val date: String,
    val url: String,
    val tags: List<String>,
    val createdDt: String,
) {
    companion object {
        fun from(release: Release): ReleaseResponse =
            ReleaseResponse(
                project = release.project,
                version = release.version,
                date = release.date,
                url = release.url,
                tags = release.tags,
                createdDt = release.createdDt,
            )
    }
}
