package com.nexusblog.persistence.dao.service.Impl;

import com.nexusblog.dto.UserDto;
import com.nexusblog.persistence.dao.repository.RoleRepository;
import com.nexusblog.persistence.dao.repository.UserRepository;
import com.nexusblog.persistence.dao.service.interfaces.UserService;
import com.nexusblog.persistence.entity.Role;
import com.nexusblog.persistence.entity.User;
import com.nexusblog.util.TbConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public  UserServiceImpl(UserRepository userRepository,
                            RoleRepository roleRepository,
                            PasswordEncoder passwordEncoder){

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void saveUser(UserDto userDto) {
        Role role = roleRepository.findByName(TbConstants.Roles.USER);

        if(role == null){
            role = roleRepository.save(new Role(TbConstants.Roles.USER));
        }

        User user = new User(
                userDto.getUsername(),
                passwordEncoder.encode(userDto.getPassword()),
                Collections.singleton(role));
        userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
