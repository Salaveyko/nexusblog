package com.nexusblog.persistence.dao.service.Impl;

import com.nexusblog.persistence.dao.repository.UserRepository;
import com.nexusblog.persistence.entity.Post;
import com.nexusblog.persistence.entity.Role;
import com.nexusblog.persistence.entity.User;
import com.nexusblog.util.TbConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Optional;

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

        User expUser = new User("admin", "encodedPassword");
        expUser.addRole(new Role(TbConstants.Roles.USER));
        expUser.addRole(new Role(TbConstants.Roles.ADMIN));
        expUser.addPost(new Post("title1","content1",new Date(), new Date()));
        expUser.addPost(new Post("title2","content2",new Date(), new Date()));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expUser));

        UserDetails actUser = userDetailsService.loadUserByUsername(username);

        verify(userRepository, times(1)).findByUsername(username);
        assertEquals(expUser, actUser);
    }
}