package com.nexusblog.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
public class PostDto {
    private Long id;
    @NotEmpty(message = "Enter Title")
    private String title;
    private String content;
    private Date created;
    private Date updated;
    private Long userId;
    public PostDto(){
        id = 0L;
        created = new Date();
        updated = new Date();
        userId = 0L;
    }
}
