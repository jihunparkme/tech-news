package com.technews.aggregate.releases.dto;

import com.technews.aggregate.releases.domain.Release;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Slf4j
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveReleaseRequest {
    public static final SaveReleaseRequest EMPTY = new SaveReleaseRequest();

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);

    private String project;
    private String version;
    private String date;
    private String url;
    private List<String> tags;
    private String createdDt;

    public Release toRelease() {
        return Release.builder()
                .project(this.project)
                .version(this.version)
                .date(this.date)
                .url(this.url)
                .tags(this.tags)
                .createdDt(this.createdDt)
                .build();
    }

    public Release toRelease(final String id) {
        return Release.builder()
                .id(id)
                .project(this.project)
                .version(this.version)
                .date(this.date)
                .url(this.url)
                .tags(this.tags)
                .createdDt(this.createdDt)
                .build();
    }

    public boolean isLatestDateVersion(final String latestReleaseDate) {
        if (StringUtils.isBlank(latestReleaseDate)) {
            return true;
        }

        try {
            final LocalDate latest = LocalDate.parse(latestReleaseDate, formatter);
            final LocalDate date = LocalDate.parse(this.date, formatter);
            return date.isAfter(latest);
        } catch (Exception e) {
            log.error("Error parsing the date. date: {}, message: {}", this.date, e.getMessage(), e);
            return false;
        }
    }

    public boolean isLatestVersion(final String version) {
        if (StringUtils.isBlank(version)) {
            return true;
        }
        return this.version.compareToIgnoreCase(version) > 0;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public boolean isNotEmpty() {
        return !this.isEmpty();
    }
}