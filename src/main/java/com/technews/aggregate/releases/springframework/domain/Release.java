package com.technews.aggregate.releases.springframework.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Release {
    public static final Release EMPTY =
            new Release("", "", "", "Apr 19, 1900", "", Collections.EMPTY_LIST);

    private String id;
    private String project;
    private String version;
    private String date;
    private String url;
    private List<String> tags;
}