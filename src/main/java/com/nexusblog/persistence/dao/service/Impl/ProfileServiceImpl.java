package com.nexusblog.persistence.dao.service.Impl;

import com.nexusblog.dto.ConverterDto;
import com.nexusblog.dto.ProfileDto;
import com.nexusblog.exceptions.ProfileNotFoundException;
import com.nexusblog.persistence.dao.repository.ProfileRepository;
import com.nexusblog.persistence.dao.service.interfaces.ProfileService;
import com.nexusblog.persistence.entity.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    @Transactional
    public ProfileDto get() throws ProfileNotFoundException {
        return get(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    @Transactional
    public ProfileDto get(String username) throws ProfileNotFoundException {
        Optional<Profile> profileOpt = profileRepository.getByUser_Username(username);
        if (profileOpt.isEmpty()) {
            throw new ProfileNotFoundException("Profile don`t found");
        }

        return ConverterDto.profileToDto(profileOpt.get());
    }

    @Override
    @Transactional
    public ProfileDto update(ProfileDto profileDto) throws ProfileNotFoundException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Profile> profileOpt = profileRepository.getByUser_Username(username);

        if (profileOpt.isEmpty()) {
            throw new ProfileNotFoundException("Profile don`t found");
        }

        Profile profile = profileOpt.get();
        profile.setName(profileDto.getName());
        profile.setSurname(profileDto.getSurname());
        profile.setBirthdate(profileDto.getBirthdate());
        profile.setMail(profileDto.getMail());

        return ConverterDto.profileToDto(profile);
    }
}
