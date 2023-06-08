package com.nexusblog.persistence.service.interfaces;

import com.nexusblog.dto.PostDto;
import com.nexusblog.exceptions.PostNotFoundException;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Set;

public interface PostsService {
    Set<PostDto> getAll();
    Set<PostDto> getMyPosts() ;
    PostDto save(PostDto postDto) throws UserPrincipalNotFoundException;
    void removeById(Long id);
    PostDto getPostById(Long id) throws PostNotFoundException;
}
