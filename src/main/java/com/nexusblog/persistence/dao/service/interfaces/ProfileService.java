package com.nexusblog.persistence.dao.service.interfaces;

import com.nexusblog.dto.ProfileDto;
import com.nexusblog.exceptions.ProfileNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileService {
    ProfileDto get(String username) throws ProfileNotFoundException;

    ProfileDto update(ProfileDto profileDto, MultipartFile file) throws ProfileNotFoundException, IOException;
}
