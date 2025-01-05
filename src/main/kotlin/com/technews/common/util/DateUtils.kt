package com.technews.common.util

import mu.KotlinLogging
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

private val logger = KotlinLogging.logger {}

object DateUtils {
    val ENGLISH_FORMATTER: DateTimeFormatter =
        DateTimeFormatter.ofPattern("[MMMM dd, yyyy][MMMM d, yyyy][MMM dd, yyyy][MMM d, yyyy][d MMMM yyyy][d MMM yyyy", Locale.ENGLISH)

    val GREENWICH_FORMATTER: DateTimeFormatter =
        DateTimeFormatter.ofPattern("[EEE, dd MMM yyyy HH:mm:ss z][EEE, d MMM yyyy HH:mm:ss z]", Locale.ENGLISH)

    val CREATED_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun parseEnglishDateFormat(date: String): String = try {
        LocalDate.parse(date, ENGLISH_FORMATTER).format(CREATED_FORMATTER)
    } catch (e: DateTimeParseException) {
        logger.error(e) { "Failed to parse date: $date" }
        today()
    }

    fun parseGreenwichToSeoul(date: String): String = try {
        val zonedDateTime = ZonedDateTime.parse(date, GREENWICH_FORMATTER)
        val withZoneSameInstant = zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"))
        withZoneSameInstant.format(CREATED_FORMATTER)
    } catch (e: DateTimeParseException) {
        logger.error(e) { "Failed to parse date: $date" }
        today()
    }

    fun today(): String =
        LocalDate.now().format(CREATED_FORMATTER)
}
