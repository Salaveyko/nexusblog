package com.nexusblog.persistence.dao.service.interfaces;

import com.nexusblog.dto.PostDto;
import com.nexusblog.exceptions.PostNotFoundException;
import com.nexusblog.persistence.entity.Post;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Set;

public interface PostsService {
    Set<PostDto> getAll();
    Set<PostDto> getMyPosts() throws UserPrincipalNotFoundException;
    PostDto save(PostDto postDto) throws UserPrincipalNotFoundException;
    void removeById(Long id);
    PostDto getPostById(Long id) throws PostNotFoundException;
}
