package com.technews.aggregate.posts.domain.repository;

import com.technews.aggregate.posts.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostsRepository extends MongoRepository<Post, String> {
    Page<Post> findAll(Pageable pageable);
}
