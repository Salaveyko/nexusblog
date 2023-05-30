package com.nexusblog.persistence.dao.service.interfaces;

import com.nexusblog.dto.ProfileDto;
import com.nexusblog.exceptions.ProfileNotFoundException;

public interface ProfileService {
    ProfileDto get() throws ProfileNotFoundException;

    ProfileDto get(String username) throws ProfileNotFoundException;

    ProfileDto update(ProfileDto profileDto) throws ProfileNotFoundException;
}
