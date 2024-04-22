package com.technews.aggregate.springframework.dto;

import com.technews.aggregate.springframework.domain.Release;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveReleaseRequest {
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
}