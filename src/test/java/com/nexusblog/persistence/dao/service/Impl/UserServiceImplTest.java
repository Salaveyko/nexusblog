package com.nexusblog.persistence.dao.service.Impl;

import com.nexusblog.dto.ConverterDto;
import com.nexusblog.dto.UserDto;
import com.nexusblog.persistence.dao.repository.RoleRepository;
import com.nexusblog.persistence.dao.repository.UserRepository;
import com.nexusblog.persistence.entity.*;
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
        Profile profile = new Profile("name", "surname", new Date());
        profile.setContacts(new ProfileContacts());
        profile.setAddress(new Address());

        user = new User("Admin", "encodedPassword");
        user.addRole(new Role(TbConstants.Roles.USER));
        user.addPost(new Post("title1","content1",new Date(), new Date()));
        user.addPost(new Post("title2","content2",new Date(), new Date()));
        user.setProfile(profile);
    }

    @Test
    void saveUser_correctAddingRoleAndEncodingPasswordThenSavingUser() {
        Role role = new Role(TbConstants.Roles.USER);
        Optional<Role> expOptRole = Optional.of(role);
        UserDto expected = ConverterDto.userToDto(user);

        when(roleRepository.findByName(TbConstants.Roles.USER)).thenReturn(expOptRole);
        when(passwordEncoder.encode(expected.getPassword())).thenReturn("encodedPassword");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(userCaptor.capture())).thenReturn(user);

        UserDto actual = userService.saveUser(expected);

        verify(roleRepository, times(1)).findByName(TbConstants.Roles.USER);
        verify(passwordEncoder, times(1)).encode(any(String.class));
        verify(userRepository, times(1)).save(any(User.class));

        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getRoles(), actual.getRoles());
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