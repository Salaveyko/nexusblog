package com.nexusblog.dto;

import com.nexusblog.persistence.entity.Comment;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    @NotEmpty(message = "Enter Title")
    private String content;
    private Date created;
    private Long postId;
    private String username;
    private Set<CommentDto> comments;
    private Long parentCommentId;
}
