package com.nexusblog.persistence.service.interfaces;

import com.nexusblog.dto.ProfileDto;
import com.nexusblog.exceptions.ProfileNotFoundException;
import com.nexusblog.persistence.entity.Profile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileService {
    ProfileDto get(String username) throws ProfileNotFoundException;

    ProfileDto update(ProfileDto profileDto, MultipartFile file)
            throws ProfileNotFoundException, IOException;

    void createVerificationToken(Profile profile, String email, String token);

    void checkVerificationToken(String token);
}
