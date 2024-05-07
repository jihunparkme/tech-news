package com.technews.aggregate.posts.dto;

import com.technews.aggregate.posts.domain.Post;
import com.technews.common.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavePostRequest {
    private String subject;
    private String title;
    private String url;
    private String category;
    private String writer;
    private String date;
    List<String> tags;
    private String createdDt;

    public boolean isLatestDatePost(final String latestPostDate) {
        if (StringUtils.isBlank(latestPostDate)) {
            return true;
        }

        try {
            final LocalDate latest = LocalDate.parse(latestPostDate, DateUtils.CREATED_FORMATTER);
            final LocalDate date = LocalDate.parse(this.date, DateUtils.CREATED_FORMATTER);
            return date.isAfter(latest);
        } catch (Exception e) {
            log.error("Error parsing the date. date: {}, message: {}", this.date, e.getMessage(), e);
            return false;
        }
    }

    public Post toPost() {
        return Post.builder()
                .subject(this.subject)
                .title(this.title)
                .category(this.category)
                .writer(this.writer)
                .date(this.date)
                .tags(this.tags)
                .url(this.url)
                .createdDt(this.createdDt)
                .build();
    }
}
