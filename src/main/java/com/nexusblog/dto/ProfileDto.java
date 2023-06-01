package com.nexusblog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {
    private Long id;
    private String avatarPath;
    private String name;
    private String surname;
    private Date birthdate;
    private ProfileContactsDto contacts;
    private AddressDto address;
    private String username;
}
