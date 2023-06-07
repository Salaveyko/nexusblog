package com.nexusblog.persistence.service.interfaces;

import com.nexusblog.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    UserDto saveUser(UserDto userDto);
    UserDto findByUsername(String username);
    void createVerificationToken(UserDto user, String token);
    void checkVerificationToken(String token, HttpServletRequest request, HttpServletResponse response);

}
