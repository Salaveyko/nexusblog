package com.nexusblog.persistence.dao.service.Impl;

import com.nexusblog.dto.ConverterDto;
import com.nexusblog.dto.UserDto;
import com.nexusblog.persistence.dao.repository.RoleRepository;
import com.nexusblog.persistence.dao.repository.UserRepository;
import com.nexusblog.persistence.dao.service.interfaces.UserService;
import com.nexusblog.persistence.entity.Profile;
import com.nexusblog.persistence.entity.Role;
import com.nexusblog.persistence.entity.User;
import com.nexusblog.util.TbConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDto saveUser(UserDto userDto) {
        Optional<Role> roleOpt = roleRepository.findByName(TbConstants.Roles.USER);
        Role role = roleOpt.orElseGet(() -> roleRepository.save(new Role(TbConstants.Roles.USER)));

        userDto.setUsername(userDto.getUsername().substring(0,1).toUpperCase()
                + userDto.getUsername().substring(1));

        User user = new User(
                userDto.getUsername(),
                passwordEncoder.encode(userDto.getPassword()));
        user.addRole(role);

        Profile profile = new Profile();
        profile.setUser(user);
        user.setProfile(profile);

        userRepository.save(user);

        return ConverterDto.userToDto(user);
    }

    @Override
    @Transactional
    public UserDto findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return ConverterDto.userToDto(user.get());
        }
        throw new UsernameNotFoundException("User not found");
    }
}
