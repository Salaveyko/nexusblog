package com.nexusblog.persistence.dao.service.Impl;

import com.nexusblog.dto.ConverterDto;
import com.nexusblog.dto.ProfileDto;
import com.nexusblog.exceptions.ProfileNotFoundException;
import com.nexusblog.persistence.dao.repository.ProfileRepository;
import com.nexusblog.persistence.dao.service.interfaces.ProfileService;
import com.nexusblog.persistence.entity.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

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
        profile.setAvatarPath(profileDto.getAvatarPath());
        profile.setName(profileDto.getName());
        profile.setSurname(profileDto.getSurname());
        profile.setBirthdate(profileDto.getBirthdate());
        profile.getContacts().setEmail(profileDto.getContacts().getEmail());
        profile.getContacts().setPhone(profileDto.getContacts().getPhone());
        profile.getAddress().setCountry(profileDto.getAddress().getCountry());
        profile.getAddress().setStatement(profileDto.getAddress().getStatement());
        profile.getAddress().setStreet(profileDto.getAddress().getStreet());
        profile.getAddress().setBuildingNumber(profileDto.getAddress().getBuildingNumber());
        profile.getAddress().setPostalCode(profileDto.getAddress().getPostalCode());

        return ConverterDto.profileToDto(profile);
    }
}
