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
import com.nexusblog.persistence.service.interfaces.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostsRepository postsRepository;


    @Override
    public CommentDto add(CommentDto commentDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!username.equals(commentDto.getUsername()))
            throw new AuthorizationServiceException("User not authorized");

        Comment comment = new Comment();

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) throw new UsernameNotFoundException("User not found");
        comment.setUser(userOpt.get());

        Optional<Post> postOpt = postsRepository.findById(commentDto.getPostId());
        if(postOpt.isEmpty()) throw new PostNotFoundException();
        comment.setPost(postOpt.get());

        if(commentDto.getParentCommentId() != null) {
            Optional<Comment> parentComment = commentRepository.findById(commentDto.getParentCommentId());
            parentComment.ifPresent(comment::setParentComment);
        }

        comment.setContent(commentDto.getContent());
        comment.setCreated(new Date());

        return ConverterDto.commentToDto(commentRepository.save(comment));
    }

    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public CommentDto getById(Long commentId) {
        Optional<Comment> commOpt = commentRepository.findById(commentId);
        if(commOpt.isEmpty()) throw new CommentNotFoundException();
        return ConverterDto.commentToDto(commOpt.get());
    }

}
