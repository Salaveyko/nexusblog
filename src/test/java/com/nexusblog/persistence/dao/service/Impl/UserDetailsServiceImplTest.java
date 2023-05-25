package com.nexusblog.persistence.dao.service.Impl;

import com.nexusblog.persistence.dao.repository.UserRepository;
import com.nexusblog.persistence.entity.Role;
import com.nexusblog.persistence.entity.User;
import com.nexusblog.util.TbConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void loadUserByUsernameTest_returnsCorrectUser() {
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

        UserDetails actUser = userDetailsService.loadUserByUsername(username);

        verify(userRepository, times(1)).findByUsername(username);
        assertEquals(expUser, actUser);
    }
}