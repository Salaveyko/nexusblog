package com.nexusblog.persistence.dao.service.interfaces;

import com.nexusblog.dto.UserDto;
import com.nexusblog.persistence.entity.User;

public interface UserService {
    void saveUser(UserDto userDto);
    User findUserByUsername(String username);
}
