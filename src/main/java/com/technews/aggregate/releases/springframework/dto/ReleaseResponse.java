package com.technews.aggregate.releases.springframework.dto;

import com.technews.aggregate.releases.springframework.domain.Release;
import lombok.*;

import java.util.List;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseResponse {
    private String project;
    private String version;
    private String date;
    private String url;
    private List<String> tags;
    private String createdDt;

    public static ReleaseResponse of(Release release) {
        return ReleaseResponse.builder()
                .project(release.getProject())
                .version(release.getVersion())
                .date(release.getDate())
                .url(release.getUrl())
                .tags(release.getTags())
                .createdDt(release.getCreatedDt())
                .build();
    }
}
