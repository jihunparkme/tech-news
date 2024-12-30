package com.technews.aggregate.releases.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "releases")
class Release(
    @Id
    val id: String? = null,
    val project: String = "",
    val version: String = "",
    val date: String = "",
    val url: String = "",
    val tags: List<String> = emptyList(),
    var shared: Boolean = false,
    val createdDt: String = "",
) {
    fun share() {
        shared = true
    }
}
