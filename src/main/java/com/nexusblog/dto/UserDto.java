package com.nexusblog.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
