package com.technews.aggregate.releases.domain

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "releases")
class Release(
    private val id: String = "",
    private val project: String = "",
    private val version: String = "",
    private val date: String = "",
    private val url: String = "",
    private val tags: List<String> = emptyList(),
    private var shared: Boolean = false,
    private val createdDt: String = "",
) {
    fun share() {
        shared = true
    }
}