package com.technews.aggregate.springframework.dto;

import com.technews.aggregate.springframework.domain.Release;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveReleaseRequest {
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);

    private String project;
    private String version;
    private String date;
    private String url;

    public Release toRelease(final String id) {
        return Release.builder()
                .id(id)
                .project(this.project)
                .version(this.version)
                .date(this.date)
                .url(this.url)
                .build();
    }

    public boolean isNewVersion(final String latestReleaseDate) {
        try {
            final LocalDate latest = LocalDate.parse(latestReleaseDate, formatter);
            final LocalDate date = LocalDate.parse(this.date, formatter);
            return date.isAfter(latest);
        } catch (Exception e) {
            log.error("Error parsing the date. date: {}, message: {}", this.date, e.getMessage(), e);
            return false;
        }
    }
}