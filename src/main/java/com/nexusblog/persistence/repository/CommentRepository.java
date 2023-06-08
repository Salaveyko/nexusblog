package com.nexusblog.persistence.repository;

import com.nexusblog.persistence.entity.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
}
