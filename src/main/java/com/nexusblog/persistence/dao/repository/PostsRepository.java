package com.nexusblog.persistence.dao.repository;

import com.nexusblog.persistence.entity.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsRepository extends CrudRepository<Post, Long> {
    Iterable<Post> findAllByUserId(Long id);
}