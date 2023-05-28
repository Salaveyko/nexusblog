package com.nexusblog.dto;

import com.nexusblog.persistence.entity.Post;
import com.nexusblog.persistence.entity.Role;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotEmpty(message = "Enter valid username")
    @Size(min = 4, message = "Username should have at least 4 characters")
    private String username;
    @NotEmpty(message = "Enter valid password")
    @Size(min = 5, message = "Password should have at least 5 characters")
    private String password;
    private String passwordConfirm;
    private boolean isEnabled;
    private Set<RoleDto> roles;
    private Set<PostDto> posts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return isEnabled == userDto.isEnabled
                && Objects.equals(id, userDto.id)
                && Objects.equals(username, userDto.username)
                && Objects.equals(password, userDto.password)
                && Objects.equals(passwordConfirm, userDto.passwordConfirm)
                && Objects.equals(roles, userDto.roles)
                && Objects.equals(posts, userDto.posts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, passwordConfirm, isEnabled, roles, posts);
    }
}
