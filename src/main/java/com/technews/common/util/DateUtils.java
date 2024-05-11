package com.technews.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@Slf4j
public class DateUtils {

    public final static DateTimeFormatter ENGLISH_FORMATTER = DateTimeFormatter.ofPattern("[MMMM dd, yyyy][MMMM d, yyyy][MMM dd, yyyy][MMM d, yyyy]", Locale.ENGLISH);
    public final static DateTimeFormatter CREATED_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String getFormattedDate(final String date) {
        try {
            final LocalDate localDate = LocalDate.parse(date, ENGLISH_FORMATTER);
            return localDate.format(CREATED_FORMATTER);
        } catch (DateTimeParseException e) {
            log.error("DateTimeParseException. {}", date);
            return StringUtils.EMPTY;
        }
    }
}
