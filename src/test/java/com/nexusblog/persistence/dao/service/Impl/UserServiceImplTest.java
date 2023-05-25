package com.nexusblog.persistence.dao.service.Impl;

import com.nexusblog.dto.UserDto;
import com.nexusblog.persistence.dao.repository.RoleRepository;
import com.nexusblog.persistence.dao.repository.UserRepository;
import com.nexusblog.persistence.entity.Role;
import com.nexusblog.persistence.entity.User;
import com.nexusblog.util.TbConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void saveUser_correctAddingRoleAndEncodingPasswordThenSavingUser() {
        UserDto userDto = new UserDto(
                null,
                "newUser",
                "newPasswd",
                "newPasswd",
                true);
        Role role = new Role(TbConstants.Roles.USER);
        User user = new User(
                userDto.getUsername(),
                "encodedPasswd",
                Collections.singleton(role));

        when(roleRepository.findByName(TbConstants.Roles.USER)).thenReturn(role);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPasswd");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(userCaptor.capture())).thenReturn(user);

        userService.saveUser(userDto);

        verify(roleRepository, times(1)).findByName(TbConstants.Roles.USER);
        verify(passwordEncoder, times(1)).encode(userDto.getPassword());
        verify(userRepository, times(1)).save(any(User.class));

        User capturedUser = userCaptor.getValue();
        assertEquals(user.getUsername(), capturedUser.getUsername());
        assertEquals(user.getUsername(), capturedUser.getUsername());
        assertEquals(user.getRoles(), capturedUser.getRoles());
        assertEquals(user.isEnabled(), capturedUser.isEnabled());
    }

    @Test
    void findUserByUsername_returnsCorrectUser() {
        String username = "admin";

        Set<Role> roles = new HashSet<>();
        roles.add(new Role(TbConstants.Roles.USER));
        roles.add(new Role(TbConstants.Roles.ADMIN));

        User expUser = new User(
                3L,
                "admin",
                "passwdAdmin",
                true,
                roles);

        when(userRepository.findByUsername(username)).thenReturn(expUser);

        User actUser = userService.findUserByUsername(username);

        verify(userRepository, times(1)).findByUsername(username);
        assertEquals(expUser, actUser);
    }
}