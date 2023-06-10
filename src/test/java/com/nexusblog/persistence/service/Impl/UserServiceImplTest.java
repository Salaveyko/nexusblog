package com.nexusblog.persistence.service.Impl;

import com.nexusblog.dto.ConverterDto;
import com.nexusblog.dto.UserDto;
import com.nexusblog.events.event.OnRegistrationCompleteEvent;
import com.nexusblog.persistence.entity.*;
import com.nexusblog.persistence.repository.RoleRepository;
import com.nexusblog.persistence.repository.UserRepository;
import com.nexusblog.util.TbConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    @Mock
    private TokenServiceImpl tokenService;
    @Mock
    private SecurityContextRepository contextRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @InjectMocks
    private UserServiceImpl userService;
    private User user;

    @BeforeEach
    void init() {
        Profile profile = new Profile("name", "surname", new Date());
        profile.setContacts(new ProfileContacts());
        profile.setAddress(new Address());
        profile.setAvatarPath("");
        Post post = new Post("title1", "content1", new Date(), new Date());


        user = new User("Admin", "encodedPassword");
        user.addRole(new Role(TbConstants.Roles.USER));
        user.addPost(post);
        profile.setUser(user);
        post.setUser(user);
        user.setProfile(profile);
    }

    @Test
    void saveUser_correctAddingRoleAndEncodingPasswordThenSavingUser() {
        Role role = new Role(TbConstants.Roles.USER);
        Optional<Role> expOptRole = Optional.of(role);
        UserDto expected = ConverterDto.userToDto(user);

        when(roleRepository.findByName(TbConstants.Roles.USER)).thenReturn(expOptRole);
        when(passwordEncoder.encode(expected.getPassword())).thenReturn("encodedPassword");

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto actual = userService.saveNewUser(expected);

        verify(roleRepository, times(1)).findByName(TbConstants.Roles.USER);
        verify(passwordEncoder, times(1)).encode(any(String.class));
        verify(userRepository, times(1)).save(any(User.class));
        verify(eventPublisher, times(1)).publishEvent(any(OnRegistrationCompleteEvent.class));

        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getRoles(), actual.getRoles());
    }


    @Nested
    class VerificationTokensTests {
        @Test
        void createVerificationToken_userEnabled() {
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

            userService.createVerificationToken(user, "token");

            verify(userRepository, times(1)).findByUsername(anyString());
            verify(tokenService, times(1)).save(any(VerificationToken.class));
        }

        @Test
        void createVerificationToken_throwUsernameNotFoundException() {
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
            assertThrows(UsernameNotFoundException.class,
                    () -> userService.createVerificationToken(user, ""));
        }

        @Test
        void enableUser_userEnabled() {
            VerificationToken verToken = new VerificationToken();
            verToken.setUser(user);
            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpServletResponse response = mock(HttpServletResponse.class);

            when(tokenService.verify(anyString())).thenReturn(verToken);

            userService.enableUser("", request, response);

            verify(userRepository, times(1)).save(any(User.class));
            verify(tokenService, times(1)).delete(verToken);
        }
    }

    @Nested
    class FindUserByUsernameTests {
        @Test
        void findUserByUsername_returnsCorrectUser() {
            String username = "admin";
            UserDto expected = ConverterDto.userToDto(user);

            when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

            UserDto actual = userService.findByUsername(username);

            verify(userRepository, times(1)).findByUsername(username);
            assertEquals(expected, actual);
        }

        @Test
        void findUserByUsername_throwUsernameNotFoundException() {
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
            assertThrows(UsernameNotFoundException.class,
                    () -> userService.findByUsername(""));
        }
    }
}