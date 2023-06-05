package com.nexusblog.persistence.dao.service.Impl;

import com.nexusblog.dto.ConverterDto;
import com.nexusblog.dto.ProfileDto;
import com.nexusblog.exceptions.ProfileNotFoundException;
import com.nexusblog.persistence.dao.repository.ProfileRepository;
import com.nexusblog.persistence.entity.Address;
import com.nexusblog.persistence.entity.Profile;
import com.nexusblog.persistence.entity.ProfileContacts;
import com.nexusblog.persistence.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
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
                "",
                "name",
                "surname",
                new Date(),
                new ProfileContacts(1L,"asd@a.as", "lolo"),
                new Address(),
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
    void update_returnSavedProfile() throws ProfileNotFoundException, IOException {
        ProfileDto expected = ConverterDto.profileToDto(profile);
        String username = profile.getUser().getUsername();

        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", new byte[0]);

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(username);
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(profileRepository.getByUser_Username(username)).thenReturn(Optional.ofNullable(profile));
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        ProfileDto actual = profileService.update(expected, multipartFile);

        verify(profileRepository, times(1)).getByUser_Username(any(String.class));
        verify(profileRepository, times(1)).save(any(Profile.class));
        assertEquals(expected, actual);
    }
}