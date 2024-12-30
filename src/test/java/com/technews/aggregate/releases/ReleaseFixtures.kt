package com.technews.aggregate.releases

import com.technews.aggregate.releases.dto.SaveReleaseRequest

fun createReleaseRequest(
    project: String = "",
    version: String = "",
    date: String = "",
    url: String = "",
    tags: List<String> = emptyList(),
    createdDt: String = "",
): SaveReleaseRequest {
    return SaveReleaseRequest(project, version, date, url, tags, createdDt)
}
