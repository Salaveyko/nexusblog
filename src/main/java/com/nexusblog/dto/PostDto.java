package com.nexusblog.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Setter
@Getter
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

    public PostDto(String title, String content, Date created, Date updated) {
        this.title = title;
        this.content = content;
        this.created = created;
        this.updated = updated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostDto postDto = (PostDto) o;
        return Objects.equals(id, postDto.id)
                && Objects.equals(title, postDto.title)
                && Objects.equals(content, postDto.content)
                && Objects.equals(created, postDto.created)
                && Objects.equals(updated, postDto.updated)
                && Objects.equals(username, postDto.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, created, updated, username);
    }
}
