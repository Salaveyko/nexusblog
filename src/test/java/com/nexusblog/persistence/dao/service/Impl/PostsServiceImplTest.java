package com.nexusblog.persistence.dao.service.Impl;

import com.nexusblog.dto.ConverterDto;
import com.nexusblog.dto.PostDto;
import com.nexusblog.exceptions.PostNotFoundException;
import com.nexusblog.nexusblog.NexusblogApplication;
import com.nexusblog.persistence.dao.repository.PostsRepository;
import com.nexusblog.persistence.dao.repository.UserRepository;
import com.nexusblog.persistence.entity.Post;
import com.nexusblog.persistence.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration
@ExtendWith(SpringExtension.class)
class PostsServiceImplTest {
    @Mock
    private PostsRepository postsRepository;
    @InjectMocks
    private PostsServiceImpl postsService;
    private Post post;

    @BeforeEach
    void init() {
        User user = new User("name", "password");
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
    void getAll_returnCorrectPostsSet() {
        Iterable<Post> returned = Collections.singleton(post);
        when(postsRepository.findAll()).thenReturn(returned);

        Set<PostDto> expected = Collections.singleton(ConverterDto.postToDto(post));
        Set<PostDto> actual = postsService.getAll();

        verify(postsRepository, times(1)).findAll();
        assertEquals(expected, actual);
    }

    @Test
    void getMyPosts_returnedCorrectUserPostsSet() {
        Iterable<Post> returned = Collections.singleton(post);

        when(postsRepository.findAllByUser_Username(any(String.class))).thenReturn(returned);

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(post.getUser().getUsername());
        SecurityContextHolder.getContext().setAuthentication(auth);

        Set<PostDto> expected = Collections.singleton(ConverterDto.postToDto(post));
        Set<PostDto> actual = postsService.getMyPosts();

        verify(postsRepository, times(1)).findAllByUser_Username(any(String.class));
        assertEquals(expected, actual);
    }

    @Test
    void save_returnedSavedUser() {
        User user = post.getUser();
        PostDto expected = ConverterDto.postToDto(post);

        when(postsRepository.save(any(Post.class))).thenReturn(post);

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(auth);

        PostDto actual = postsService.save(expected);

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