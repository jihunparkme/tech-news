package com.technews.aggregate.posts.domain.repository;

import com.technews.aggregate.posts.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostsRepository extends MongoRepository<Post, String> {

    @Aggregation(pipeline = {
            "{ '$match': { 'category' : ?0 } }",
            "{ '$sort' : { 'date' : -1 } }",
            "{ '$limit' : 1 }"
    })
    List<Post> findByCategoryOrderByDateDescLimitOne(String category);

    Page<Post> findBySubject(String subject, PageRequest pageable);

    Page<Post> findBySubjectAndCategoryIn(String subject, List<String> category, PageRequest pageable);

    List<Post> findBySharedTrue();
}
