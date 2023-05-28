package com.nexusblog.persistence.dao.service.Impl;

import com.nexusblog.dto.ConverterDto;
import com.nexusblog.dto.PostDto;
import com.nexusblog.exceptions.PostNotFoundException;
import com.nexusblog.persistence.dao.repository.PostsRepository;
import com.nexusblog.persistence.entity.Post;
import com.nexusblog.persistence.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostsServiceImplTest {
    @Mock
    private PostsRepository postsRepository;
    @InjectMocks
    private PostsServiceImpl postsService;
    private Post post;

    @BeforeEach
    void init() {
        User user = new User("name", "passwd");
        user.setId(1L);
        post = new Post(
                1L,
                "title",
                "content",
                new Date(),
                new Date(),
                user
        );
    }

    @Test
    void getAll_returnedCorrectPostsSet() {
        Iterable<Post> returned = Collections.singleton(post);
        when(postsRepository.findAll()).thenReturn(returned);

        Set<PostDto> expected = Collections.singleton(ConverterDto.postToDto(post));
        Set<PostDto> actual = postsService.getAll();

        verify(postsRepository, times(1)).findAll();
        assertEquals(expected, actual);
    }

    @Test
    void getMyPosts_returnedCorrectUserPostsSet() throws UserPrincipalNotFoundException {
        User user = post.getUser();
        Iterable<Post> returned = Collections.singleton(post);

        when(postsRepository.findAllByUserId(any(Long.class))).thenReturn(returned);

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Set<PostDto> expected = Collections.singleton(ConverterDto.postToDto(post));
        Set<PostDto> actual = postsService.getMyPosts();

        verify(postsRepository, times(1)).findAllByUserId(user.getId());
        assertEquals(expected, actual);

        SecurityContextHolder.clearContext();
    }

    @Test
    void save() throws UserPrincipalNotFoundException {
        User user = post.getUser();
        PostDto expected = ConverterDto.postToDto(post);
        PostDto actual = new PostDto(
                expected.getTitle(),
                expected.getContent(),
                expected.getCreated(),
                expected.getUpdated());

        when(postsRepository.save(any(Post.class))).thenReturn(post);

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(auth);

        actual = postsService.save(actual);

        verify(postsRepository, times(1)).save(any(Post.class));
        assertEquals(expected, actual);

        SecurityContextHolder.clearContext();
    }

    @Test
    void removeById_correctRemovingPost() {
        Long id = 1L;

        postsService.removeById(id);
        verify(postsRepository, times(1)).deleteById(id);
    }

    @Test
    void getPostById_return() throws PostNotFoundException {
        PostDto expected = ConverterDto.postToDto(post);

        when(postsRepository.findById(any(Long.class))).thenReturn(Optional.of(post));

        PostDto actual = postsService.getPostById(1L);

        verify(postsRepository, times(1)).findById(any(Long.class));
        assertEquals(expected, actual);
    }
}