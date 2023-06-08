package com.nexusblog.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
}
