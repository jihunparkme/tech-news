package com.technews.aggregate.posts.spring.dto;

import com.technews.aggregate.posts.domain.Post;
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
public class SaveSpringPostRequest {

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.ENGLISH);

    private String subject;
    private String title;
    private String url;
    private String category;
    private String writer;
    private String date;
    List<String> tags;

    public boolean isLatestDatePost(final String latestPostDate) {
        if (StringUtils.isBlank(latestPostDate)) {
            return true;
        }

        try {
            final LocalDate latest = LocalDate.parse(latestPostDate, formatter);
            final LocalDate date = LocalDate.parse(this.date, formatter);
            return date.isAfter(latest);
        } catch (Exception e) {
            log.error("Error parsing the date. date: {}, message: {}", this.date, e.getMessage(), e);
            return false;
        }
    }

    public Post toRelease() {
        return Post.builder()
                .subject(this.subject)
                .title(this.title)
                .category(this.category)
                .writer(this.writer)
                .date(this.date)
                .tags(this.tags)
                .url(this.url)
                .build();
    }
}
