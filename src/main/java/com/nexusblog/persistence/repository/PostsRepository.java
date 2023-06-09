package com.nexusblog.persistence.repository;

import com.nexusblog.persistence.entity.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsRepository extends CrudRepository<Post, Long> {
    Iterable<Post> findAllByUser_Username(String username);
}