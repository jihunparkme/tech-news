package com.technews.scheduler.dto

data class JavaInsidePostInfo(
    val writer: String = "",
    val date: String = "",
)

data class OracleJavaBlogPostInfo(
    val title: String = "",
    val url: String = "",
    val writer: String = "",
)

data class SpringBlogMeta(
    val category: String = "",
    val writer: String = "",
    val date: String = "",
)
