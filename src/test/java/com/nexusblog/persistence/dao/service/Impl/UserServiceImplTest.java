package com.nexusblog.persistence.dao.service.Impl;

import com.nexusblog.dto.ConverterDto;
import com.nexusblog.dto.UserDto;
import com.nexusblog.persistence.dao.repository.RoleRepository;
import com.nexusblog.persistence.dao.repository.UserRepository;
import com.nexusblog.persistence.entity.Post;
import com.nexusblog.persistence.entity.Role;
import com.nexusblog.persistence.entity.User;
import com.nexusblog.util.TbConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    private User user;
    
    @BeforeEach
    void init(){
        user = new User("admin", "encodedPassword");
        user.addRole(new Role(TbConstants.Roles.USER));
        user.addRole(new Role(TbConstants.Roles.ADMIN));
        user.addPost(new Post("title1","content1",new Date(), new Date()));
        user.addPost(new Post("title2","content2",new Date(), new Date()));
    }

    @Test
    void saveUser_correctAddingRoleAndEncodingPasswordThenSavingUser() {
        Role role = new Role(TbConstants.Roles.USER);
        Optional<Role> expOptRole = Optional.of(role);
        UserDto userDto = ConverterDto.userToDto(user);

        when(roleRepository.findByName(TbConstants.Roles.USER)).thenReturn(expOptRole);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(userCaptor.capture())).thenReturn(user);

        userService.saveUser(userDto);

        verify(roleRepository, times(1)).findByName(TbConstants.Roles.USER);
        verify(passwordEncoder, times(1)).encode(userDto.getPassword());
        verify(userRepository, times(1)).save(any(User.class));

        User capturedUser = userCaptor.getValue();
        assertEquals(user.getUsername(), capturedUser.getUsername());
        assertEquals(user.getUsername(), capturedUser.getUsername());
        assertEquals(user.isEnabled(), capturedUser.isEnabled());
        assertFalse(capturedUser.getRoles().isEmpty());
    }

    @Test
    void findUserByUsername_returnsCorrectUser() {
        String username = "admin";
        UserDto expected = ConverterDto.userToDto(user);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDto actual = userService.findByUsername(username);

        verify(userRepository, times(1)).findByUsername(username);
        assertEquals(expected, actual);
    }
}