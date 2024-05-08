package com.technews.aggregate.releases.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collections;
import java.util.List;

@Document(collection = "releases")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Release {
    public static final Release EMPTY =
            new Release("", "", "", "", "", Collections.EMPTY_LIST, false, "");

    private String id;
    private String project;
    private String version;
    private String date;
    private String url;
    private List<String> tags;

    private boolean shared;
    private String createdDt;
}