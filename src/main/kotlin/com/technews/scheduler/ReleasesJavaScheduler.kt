package com.technews.scheduler

import com.technews.aggregate.releases.dto.SaveReleaseRequest
import com.technews.aggregate.releases.service.ReleasesSchedulerService
import com.technews.common.constant.JdkVersion
import com.technews.common.util.DateUtils
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

private val logger = KotlinLogging.logger { }

@Component
class ReleasesJavaScheduler(
    private val releasesSchedulerService: ReleasesSchedulerService,
) {
    @Scheduled(cron = "0 0 1 * * ?")
    fun runScheduler() {
        searchJdkReleases(JdkVersion.JDK_8)
        searchJdkReleases(JdkVersion.JDK_11)
        searchJdkReleases(JdkVersion.JDK_17)
        searchJdkReleases(JdkVersion.JDK_21)
    }

    private fun searchJdkReleases(jdk: JdkVersion) {
        val jdkLatestRelease = releasesSchedulerService.findLatestRelease(jdk.value)
        val jdkReleases = getRelease(jdk)
        jdkReleases.filter { it.isLatestVersion(jdkLatestRelease.version) }
            .forEach { releasesSchedulerService.insertRelease(it) }
    }

    companion object {
        private const val ORACLE_BASE_URL = "https://www.oracle.com"

        private fun getRelease(jdk: JdkVersion): List<SaveReleaseRequest> = try {
            if (jdk == JdkVersion.JDK_8) {
                getLowerJdk8ReleaseInfo(jdk.value, jdk.url)
            } else {
                getExceededJdk8ReleaseInfo(jdk.value, jdk.url)
            }
        } catch (e: Exception) {
            logger.error(e.message, e)
            emptyList()
        }

        private fun getExceededJdk8ReleaseInfo(project: String, url: String): List<SaveReleaseRequest> {
            val result = mutableListOf<SaveReleaseRequest>()

            val doc: Document = Jsoup.connect(url).get()
            val items: Elements = doc.select(".obullets")
            for (item in items) {
                val liElements = item.select("li")
                result.addAll(getReleaseList(project, liElements))
            }
            return result.filter { it.isNotEmpty }
                .sortedByDescending { it.version }
        }

        private fun getLowerJdk8ReleaseInfo(project: String, url: String): List<SaveReleaseRequest> {
            val result = mutableListOf<SaveReleaseRequest>()

            val doc: Document = Jsoup.connect(url).get()
            val items: Elements = doc.select(".col-item")
            for (item in items) {
                val liElements = item.select("li")
                result.addAll(getReleaseList(project, liElements))
            }
            return result.filter { it.isNotEmpty }
                .sortedByDescending { it.version }
        }

        private fun getReleaseList(project: String, liElements: Elements): List<SaveReleaseRequest> {
            val result = mutableListOf<SaveReleaseRequest>()
            for (li in liElements) {
                val release = getRelease(project, li)
                if (release.isEmpty) continue
                result.add(release)
            }
            return result
        }

        private fun getRelease(project: String, li: Element): SaveReleaseRequest {
            val liText = li.text()
            if (!liText.contains("GA")) return SaveReleaseRequest.EMPTY

            return try {
                val version = liText.split(" \\(".toRegex())[0]
                val href = li.select("a").first()?.attr("href") ?: return SaveReleaseRequest.EMPTY
                val gaUrl = ORACLE_BASE_URL + href
                SaveReleaseRequest(
                    createdDt = LocalDate.now().format(DateUtils.CREATED_FORMATTER),
                    tags = listOf(project, "java", "release"),
                    project = project,
                    version = version,
                    url = gaUrl
                )
            } catch (e: Exception) {
                logger.error("Not found matching version.")
                SaveReleaseRequest.EMPTY
            }
        }
    }
}
