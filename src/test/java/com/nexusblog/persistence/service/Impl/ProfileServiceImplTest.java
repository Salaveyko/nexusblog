package com.nexusblog.persistence.service.Impl;

import com.nexusblog.dto.ConverterDto;
import com.nexusblog.dto.ProfileDto;
import com.nexusblog.events.event.OnChangeEmailEvent;
import com.nexusblog.exceptions.ProfileNotFoundException;
import com.nexusblog.persistence.entity.*;
import com.nexusblog.persistence.repository.ProfileRepository;
import com.nexusblog.persistence.service.interfaces.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ContextConfiguration
@ExtendWith(SpringExtension.class)
class ProfileServiceImplTest {
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private TokenService tokenService;
    @InjectMocks
    private ProfileServiceImpl profileService;
    private Profile profile;

    @BeforeEach
    void init() {
        User user = new User("username", "password");
        user.setId(1L);
        profile = new Profile(
                1L,
                "",
                "name",
                "surname",
                new Date(),
                new ProfileContacts(1L, "asd@a.as", "lolo"),
                new Address(),
                user
        );
        user.setProfile(profile);
    }

    @Nested
    class GetProfileTests {
        @Test
        void get_returnsCorrectProfile() {
            ProfileDto expected = ConverterDto.profileToDto(profile);
            String username = profile.getUser().getUsername();

            when(profileRepository.getByUser_Username(username)).thenReturn(Optional.of(profile));

            ProfileDto actual = profileService.get(username);

            verify(profileRepository, times(1)).getByUser_Username(any(String.class));
            assertEquals(expected, actual);
        }

        @Test
        void get_throwProfileNotFoundException() {
            when(profileRepository.getByUser_Username(anyString())).thenReturn(Optional.empty());
            assertThrows(ProfileNotFoundException.class,
                    () -> profileService.get("nonExistentUser"));
        }
    }

    @Nested
    class UpdateProfileTests {

        @Test
        void update_returnSavedProfile() {
            ProfileDto expected = ConverterDto.profileToDto(profile);
            ProfileDto toOperate = ConverterDto.profileToDto(profile);
            toOperate.getContacts().setEmail("new email");
            String username = profile.getUser().getUsername();

            MockMultipartFile multipartFile = new MockMultipartFile(
                    "file", "test.jpg", "image/jpeg", new byte[0]);

            Authentication auth = mock(Authentication.class);
            when(auth.getName()).thenReturn(username);
            SecurityContextHolder.getContext().setAuthentication(auth);

            when(profileRepository.getByUser_Username(username)).thenReturn(Optional.of(profile));
            when(profileRepository.save(any(Profile.class))).thenReturn(profile);

            ProfileDto actual;
            try {
                actual = profileService.update(toOperate, multipartFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            verify(profileRepository, times(1)).getByUser_Username(anyString());
            verify(profileRepository, times(1)).save(any(Profile.class));
            verify(eventPublisher, times(1)).publishEvent(any(OnChangeEmailEvent.class));

            assertEquals(expected, actual);
        }

        @Test
        void update_throwProfileNotFoundException() {
            MockMultipartFile multipartFile = new MockMultipartFile(
                    "file", "test.jpg", "image/jpeg", new byte[0]);

            Authentication auth = mock(Authentication.class);
            when(auth.getName()).thenReturn("");
            SecurityContextHolder.getContext().setAuthentication(auth);

            when(profileRepository.getByUser_Username(anyString())).thenReturn(Optional.empty());

            assertThrows(ProfileNotFoundException.class,
                    () -> profileService.update(new ProfileDto(), multipartFile));
        }
    }

    @Nested
    class VerificationTokenTests {
        @Test
        void createVerificationToken_createdToken() {
            when(profileRepository.findById(anyLong())).thenReturn(Optional.of(profile));
            when(tokenService.save(any(VerificationToken.class))).thenReturn(any(VerificationToken.class));
            profileService.createVerificationToken(profile, "mail", "token");

            verify(profileRepository, times(1)).findById(anyLong());
            verify(tokenService, times(1)).save(any(VerificationToken.class));
        }
        @Test
        void createVerificationToken_throwUserNotFoundException(){
            when(profileRepository.findById(anyLong())).thenReturn(Optional.empty());
            assertThrows(UsernameNotFoundException.class,
                    () -> profileService.createVerificationToken(new Profile(), "", ""));
        }
        @Test
        void changeEmail_tokenConfirmedEmailChanged(){
            VerificationToken token = new VerificationToken();
            token.setUser(profile.getUser());
            when(tokenService.verify(anyString())).thenReturn(token);

            profileService.changeEmail("");

            verify(profileRepository, times(1)).save(any(Profile.class));
            verify(tokenService, times(1)).delete(any(VerificationToken.class));
        }
    }
}