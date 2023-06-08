package com.nexusblog.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    @NotEmpty(message = "Enter Title")
    private String title;
    private String content;
    private Date created;
    private Date updated;
    private String username;
    private String avatarPath;
    private Set<CommentDto> comments;

    public PostDto(String title, String content, Date created, Date updated) {
        this.title = title;
        this.content = content;
        this.created = created;
        this.updated = updated;
        this.comments = new HashSet<>();
    }
}
