package com.nexusblog.persistence.dao.service.Impl;

import com.google.common.collect.Lists;
import com.nexusblog.dto.PostDto;
import com.nexusblog.exceptions.PostNotFoundException;
import com.nexusblog.persistence.dao.repository.PostsRepository;
import com.nexusblog.persistence.dao.service.interfaces.PostsService;
import com.nexusblog.persistence.entity.Post;
import com.nexusblog.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class PostsServiceImpl implements PostsService {

    private final PostsRepository postsRepository;

    @Autowired
    public PostsServiceImpl(PostsRepository postsRepository){
        this.postsRepository = postsRepository;
    }

    private User getCurrentUser() throws UserPrincipalNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        } else {
            throw new UserPrincipalNotFoundException("User principal not found");
        }
    }

    @Override
    public List<Post> getAll() {
        return Lists.newArrayList(postsRepository.findAll());
    }

    @Override
    public List<Post> getMyPosts() throws UserPrincipalNotFoundException {
        User user = getCurrentUser();
        return Lists.newArrayList(postsRepository.findAllByUserId(user.getId()));
    }

    @Override
    public void save(PostDto postDto) throws UserPrincipalNotFoundException {
        Post post = new Post();

        if(postDto.getId() != 0) {
            Optional<Post> postOpt = postsRepository.findById(postDto.getId());
            if (postOpt.isPresent()) {
                post = postOpt.get();
            }
        } else {
            post.setCreated(new Timestamp(System.currentTimeMillis()));
            post.setUser(getCurrentUser());
        }

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setUpdated(postDto.getUpdated());

        postsRepository.save(post);
    }

    @Override
    public void removeById(Long id) {
        postsRepository.deleteById(id);
    }

    @Override
    public Post getPostById(Long id) throws PostNotFoundException {
        Optional<Post> post = postsRepository.findById(id);
        if (post.isPresent()) {
            return post.get();
        }
        throw new PostNotFoundException("Post not available");
    }
}
