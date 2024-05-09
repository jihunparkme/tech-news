package com.technews.aggregate.subscribe.dto;

import com.technews.aggregate.posts.domain.Post;
import com.technews.aggregate.releases.domain.Release;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriberMailContents {
    private List<Release> releaseOfSpring;
    private List<Release> releaseOfJava;

    private List<Post> postOfSpring;
    private List<Post> postOfJava;
}
