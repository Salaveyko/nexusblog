package com.nexusblog.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileContactsDto {
    private Long id;
    private String phone;
    @Email(message = "Incorrect e-mail address")
    private String email;
}
