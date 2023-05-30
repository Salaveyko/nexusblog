package com.nexusblog.persistence.dao.service.Impl;

import com.nexusblog.dto.ConverterDto;
import com.nexusblog.dto.ProfileDto;
import com.nexusblog.exceptions.ProfileNotFoundException;
import com.nexusblog.persistence.dao.repository.ProfileRepository;
import com.nexusblog.persistence.entity.Profile;
import com.nexusblog.persistence.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration
@ExtendWith(SpringExtension.class)
class ProfileServiceImplTest {
    @Mock
    private ProfileRepository profileRepository;
    @InjectMocks
    private ProfileServiceImpl profileService;
    private Profile profile;

    @BeforeEach
    void init(){
        User user = new User("username", "password");
        user.setId(1L);
        profile = new Profile(
                1L,
                "name",
                "surname",
                "e-mail@mail.com",
                new Date(),
                user
        );
    }

    @Test
    void get() throws ProfileNotFoundException {
        ProfileDto expected = ConverterDto.profileToDto(profile);
        String username = profile.getUser().getUsername();

        when(profileRepository.getByUser_Username(username)).thenReturn(Optional.ofNullable(profile));

        ProfileDto actual = profileService.get(username);

        verify(profileRepository, times(1)).getByUser_Username(any(String.class));
        assertEquals(expected, actual);
    }

    @Test
    void update_returnSavedProfile() throws ProfileNotFoundException {
        ProfileDto expected = ConverterDto.profileToDto(profile);
        String username = profile.getUser().getUsername();

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(username);
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(profileRepository.getByUser_Username(username)).thenReturn(Optional.ofNullable(profile));

        ProfileDto actual = profileService.update(expected);

        verify(profileRepository, times(1)).getByUser_Username(any(String.class));
        assertEquals(expected, actual);
    }
}