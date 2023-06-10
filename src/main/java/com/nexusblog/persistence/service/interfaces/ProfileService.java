package com.nexusblog.persistence.service.interfaces;

import com.nexusblog.dto.ProfileDto;
import com.nexusblog.persistence.entity.Profile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileService {
    ProfileDto get(String username);

    ProfileDto update(ProfileDto profileDto, MultipartFile file) throws IOException;

    void createVerificationToken(Profile profile, String email, String token);

    void changeEmail(String token);
}
