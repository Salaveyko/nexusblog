package com.nexusblog.persistence.service.interfaces;

import com.nexusblog.dto.UserDto;
import com.nexusblog.persistence.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    UserDto saveNewUser(UserDto userDto);
    UserDto findByUsername(String username);
    void createVerificationToken(User user, String token);
    void checkVerificationToken(String token, HttpServletRequest request, HttpServletResponse response);

}
