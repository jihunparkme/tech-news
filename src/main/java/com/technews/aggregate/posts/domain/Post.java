package com.technews.aggregate.posts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "posts")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    public static final Post EMPTY = new Post();

    private String id;
    private String subject;
    private String title;
    private String url;

    private String category;
    private String writer;
    private String date;
    List<String> tags;
}
