package com.technews.common.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtils {

    public final static DateTimeFormatter ENGLISH_FORMATTER_1 = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
    public final static DateTimeFormatter ENGLISH_FORMATTER_2 = DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.ENGLISH);
    public final static DateTimeFormatter CREATED_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String getFormattedDate(final String date, final DateTimeFormatter formatter) {
        try {
            final LocalDate localDate = LocalDate.parse(date, formatter);
            return localDate.format(CREATED_FORMATTER);
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }
}
