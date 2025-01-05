package com.technews.scheduler.releases

import com.technews.aggregate.releases.dto.SaveReleaseRequest
import com.technews.aggregate.releases.service.ReleasesSchedulerService
import com.technews.common.constant.JdkVersion
import com.technews.common.util.DateUtils
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

@Component
class ReleasesJavaScheduler(
    private val releasesSchedulerService: ReleasesSchedulerService,
) {
    @Scheduled(cron = "0 0 1 * * ?")
    fun runSchedule() {
        listOf(
            JdkVersion.JDK_8,
            JdkVersion.JDK_11,
            JdkVersion.JDK_17,
            JdkVersion.JDK_21,
            JdkVersion.JDK_23,
        ).forEach { searchJdkReleases(it) }
    }

    private fun searchJdkReleases(jdk: JdkVersion) {
        val jdkLatestRelease = releasesSchedulerService.findLatestRelease(jdk.value)
        getRelease(jdk)
            .filter { it.isLatestVersion(jdkLatestRelease.version) }
            .forEach { releasesSchedulerService.insertRelease(it) }
    }

    companion object {
        private const val ORACLE_BASE_URL = "https://www.oracle.com"

        private fun getRelease(jdk: JdkVersion): List<SaveReleaseRequest> =
            runCatching {
                when (jdk) {
                    JdkVersion.JDK_8 -> fetchReleaseInfo(jdk.value, jdk.url, ".col-item")
                    else -> fetchReleaseInfo(jdk.value, jdk.url, ".obullets")
                }
            }.getOrElse {
                logger.error("Failed to fetch posts: ${it.message}", it)
                emptyList()
            }

        private fun fetchReleaseInfo(project: String, url: String, selector: String): List<SaveReleaseRequest> {
            val doc = Jsoup.connect(url).get()
            val items = doc.select(selector)
            return items.flatMap { item ->
                item.select("li")
                    .mapNotNull { li -> parseRelease(project, li) }
            }
                .filter { it.isNotEmpty }
                .sortedByDescending { it.version }
        }

        private fun parseRelease(project: String, li: Element): SaveReleaseRequest? {
            val liText = li.text()
            if (!liText.contains("GA")) return null

            return runCatching {
                val version = liText.split(" \\(".toRegex())[0]
                val href = li.select("a").firstOrNull()?.attr("href") ?: return null
                val gaUrl = ORACLE_BASE_URL + href
                SaveReleaseRequest(
                    createdDt = LocalDate.now().format(DateUtils.CREATED_FORMATTER),
                    tags = listOf(project, "Java", "JDK", "Release"),
                    project = project,
                    version = version,
                    url = gaUrl,
                )
            }.getOrElse {
                logger.error("Not found matching version.")
                SaveReleaseRequest.EMPTY
            }
        }
    }
}
