package com.nexusblog.persistence.service.Impl;

import com.nexusblog.dto.ConverterDto;
import com.nexusblog.dto.PostDto;
import com.nexusblog.exceptions.PostNotFoundException;
import com.nexusblog.persistence.repository.PostsRepository;
import com.nexusblog.persistence.service.interfaces.PostsService;
import com.nexusblog.persistence.entity.Post;
import com.nexusblog.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class PostsServiceImpl implements PostsService {

    private final PostsRepository postsRepository;

    @Override
    @Transactional
    public Set<PostDto> getAll() {
        return StreamSupport
                .stream(postsRepository.findAll().spliterator(), false)
                .map(ConverterDto::postToDto)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public Set<PostDto> getMyPosts(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return StreamSupport
                .stream(postsRepository.findAllByUser_Username(username).spliterator(), false)
                .map(ConverterDto::postToDto)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public PostDto save(PostDto postDto) {
        Post post = new Post();

        if(postDto.getId() != null) {
            Optional<Post> postOpt = postsRepository.findById(postDto.getId());
            if (postOpt.isPresent()) {
                post = postOpt.get();
            }
        } else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            post.setUser((User)auth.getPrincipal());
            post.setCreated(new Timestamp(System.currentTimeMillis()));
        }

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setUpdated(new Timestamp(System.currentTimeMillis()));

        return ConverterDto.postToDto(postsRepository.save(post));
    }

    @Override
    @Transactional
    public void removeById(Long id) {
        postsRepository.deleteById(id);
    }

    @Override
    @Transactional
    public PostDto getPostById(Long id) {
        Optional<Post> post = postsRepository.findById(id);
        if (post.isEmpty()) throw new PostNotFoundException();

        return ConverterDto.postToDto(post.get());
    }
}
