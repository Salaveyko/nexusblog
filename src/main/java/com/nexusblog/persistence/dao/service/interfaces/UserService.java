package com.nexusblog.persistence.dao.service.interfaces;

import com.nexusblog.dto.UserDto;
import com.nexusblog.persistence.entity.User;

import java.util.Optional;

public interface UserService {
    UserDto saveUser(UserDto userDto);
    UserDto findByUsername(String username);
}
