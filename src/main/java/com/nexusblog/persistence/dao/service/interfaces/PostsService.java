package com.nexusblog.persistence.dao.service.interfaces;

import com.nexusblog.dto.PostDto;
import com.nexusblog.exceptions.PostNotFoundException;
import com.nexusblog.persistence.entity.Post;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

public interface PostsService {
    List<Post> getAll();
    List<Post> getMyPosts() throws UserPrincipalNotFoundException;
    void save(PostDto postDto) throws UserPrincipalNotFoundException;
    void removeById(Long id);
    Post getPostById(Long id) throws PostNotFoundException;
}
