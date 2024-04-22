package com.technews.aggregate.springframework.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Release {
    private String id;
    private String project;
    private String version;
    private String date;
    private String url;
}