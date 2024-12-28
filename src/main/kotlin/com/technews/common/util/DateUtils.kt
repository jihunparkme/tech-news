package com.technews.common.util

import mu.KotlinLogging
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

private val logger = KotlinLogging.logger {}

object DateUtils {
    val ENGLISH_FORMATTER: DateTimeFormatter =
        DateTimeFormatter.ofPattern("[MMMM dd, yyyy][MMMM d, yyyy][MMM dd, yyyy][MMM d, yyyy]", Locale.ENGLISH)

    val CREATED_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun getFormattedDate(date: String): String = try {
        LocalDate.parse(date, ENGLISH_FORMATTER).format(CREATED_FORMATTER)
    } catch (e: DateTimeParseException) {
        logger.error(e) { "Failed to parse date: $date" }
        LocalDate.now().format(CREATED_FORMATTER)
    }
}
