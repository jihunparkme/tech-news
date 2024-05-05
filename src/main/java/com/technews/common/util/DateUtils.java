package com.technews.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;

@Slf4j
public class DateUtils {

    public final static DateTimeFormatter ENGLISH_FORMATTER_1 = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
    public final static DateTimeFormatter ENGLISH_FORMATTER_2 = DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.ENGLISH);
    public final static DateTimeFormatter CREATED_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final List<DateTimeFormatter> ENGLISH_FORMATTERS = Arrays.asList(
            ENGLISH_FORMATTER_1, ENGLISH_FORMATTER_2
    );

    public static String getFormattedDate(final String date) {
        for (DateTimeFormatter formatter : ENGLISH_FORMATTERS) {
            try {
                final LocalDate localDate = LocalDate.parse(date, formatter);
                return localDate.format(CREATED_FORMATTER);
            } catch (DateTimeParseException e) {
                log.error("DateTimeParseException. {}", date);
            }
        }

        return StringUtils.EMPTY;
    }
}
