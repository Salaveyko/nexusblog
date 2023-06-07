package com.nexusblog.persistence.service.Impl;

import com.nexusblog.dto.ConverterDto;
import com.nexusblog.dto.ProfileDto;
import com.nexusblog.exceptions.ProfileNotFoundException;
import com.nexusblog.persistence.repository.ProfileRepository;
import com.nexusblog.persistence.service.interfaces.ProfileService;
import com.nexusblog.persistence.entity.Profile;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    @Value("${upload.path}")
    private String uploadPath;

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
    public ProfileDto update(ProfileDto profileDto, MultipartFile file) throws ProfileNotFoundException, IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Profile> profileOpt = profileRepository.getByUser_Username(username);

        if (profileOpt.isEmpty()) {
            throw new ProfileNotFoundException("Profile don`t found");
        }

        if(!file.isEmpty()) {
            profileDto.setAvatarPath(updateFile(file, profileDto.getAvatarPath()));
        }

        Profile profile = profileRepository.save(
                ConverterDto.profileFromDto(profileOpt.get(), profileDto));

        return ConverterDto.profileToDto(profile);
    }

    private String updateFile(MultipartFile file, String oldFilePath) throws IOException {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            File uploadDir = new File(uploadPath + "/avatar");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            File outputFile = new File(uploadDir + "/" + resultFilename);

            Thumbnails.of(file.getInputStream())
                    .size(200, 200)
                    .toFile(outputFile);

            if (!oldFilePath.isEmpty()) {
                File fileToDelete = new File(uploadDir + "/" + oldFilePath);
                fileToDelete.delete();
            }

            return resultFilename;
        }
        return "";
    }
}
