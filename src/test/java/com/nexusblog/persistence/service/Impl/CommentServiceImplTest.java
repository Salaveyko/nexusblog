package com.nexusblog.persistence.service.Impl;

import com.nexusblog.dto.CommentDto;
import com.nexusblog.dto.ConverterDto;
import com.nexusblog.exceptions.CommentNotFoundException;
import com.nexusblog.exceptions.PostNotFoundException;
import com.nexusblog.persistence.entity.Comment;
import com.nexusblog.persistence.entity.Post;
import com.nexusblog.persistence.entity.User;
import com.nexusblog.persistence.repository.CommentRepository;
import com.nexusblog.persistence.repository.PostsRepository;
import com.nexusblog.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ContextConfiguration
@ExtendWith(SpringExtension.class)
class CommentServiceImplTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostsRepository postsRepository;
    @InjectMocks
    private CommentServiceImpl commentService;
    private Comment comment;

    @BeforeEach
    void init() {
        User user = new User("user", "passwd");
        Post post = new Post("title", "content", new Date(), new Date());
        comment = new Comment(
                2L,
                "content",
                new Date(),
                post,
                user,
                new HashSet<>(),
                null);

        user.addPost(post);
        post.setUser(user);
    }

    @Nested
    class CommentGettersTests {
        @Test
        void getById_correctCommentReturns() {
            when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

            CommentDto expected = ConverterDto.commentToDto(comment);
            CommentDto actual = commentService.getById(1L);

            verify(commentRepository, times(1)).findById(anyLong());
            assertEquals(expected, actual);
        }

        @Test
        void getById_CommentNotFoundException() {
            when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());
            assertThrows(CommentNotFoundException.class, () -> commentService.getById(1L));
        }
    }

    @Nested
    class AddCommentTests {
        @Test
        void add_correctAddedAndReturnedUser() {
            String username = comment.getUser().getUsername();
            Authentication auth = mock(Authentication.class);
            when(auth.getName()).thenReturn(username);
            SecurityContextHolder.getContext().setAuthentication(auth);

            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(comment.getUser()));
            when(postsRepository.findById(anyLong())).thenReturn(Optional.of(comment.getPost()));
            when(commentRepository.findById(anyLong())).thenReturn((Optional.empty()));
            when(commentRepository.save(any(Comment.class))).thenReturn(comment);

            CommentDto expected = ConverterDto.commentToDto(comment);
            CommentDto actual = commentService.add(expected);

            verify(userRepository, times(1)).findByUsername(expected.getUsername());
            verify(postsRepository, times(1)).findById(expected.getPostId());
            verify(commentRepository, times(1)).findById(anyLong());
            verify(commentRepository, times(1)).save(any(Comment.class));

            assertEquals(expected, actual);
        }

        @Test
        void add_throwAuthorizationServiceException() {
            Authentication auth = mock(Authentication.class);
            when(auth.getName()).thenReturn("");
            SecurityContextHolder.getContext().setAuthentication(auth);

            assertThrows(AuthorizationServiceException.class,
                    () -> commentService.add(ConverterDto.commentToDto(comment)));
        }
        @Test
        void add_throwUsernameNotFoundException() {
            Authentication auth = mock(Authentication.class);
            when(auth.getName()).thenReturn(comment.getUser().getUsername());
            SecurityContextHolder.getContext().setAuthentication(auth);

            when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
            assertThrows(UsernameNotFoundException.class,
                    () -> commentService.add(ConverterDto.commentToDto(comment)));
        }
        @Test
        void add_throwPostNotFoundException() {
            Authentication auth = mock(Authentication.class);
            when(auth.getName()).thenReturn(comment.getUser().getUsername());
            SecurityContextHolder.getContext().setAuthentication(auth);

            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(comment.getUser()));
            when(postsRepository.findById(anyLong())).thenReturn(Optional.empty());
            assertThrows(PostNotFoundException.class,
                    () -> commentService.add(ConverterDto.commentToDto(comment)));
        }
    }

    @Test
    void deleteById_userRemoved() {
        commentService.deleteById(1L);
        verify(commentRepository, times(1)).deleteById(anyLong());
    }
}