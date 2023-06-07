package com.nexusblog.persistence.service.Impl;

import com.nexusblog.dto.ConverterDto;
import com.nexusblog.dto.ProfileDto;
import com.nexusblog.events.event.OnChangeEmailEvent;
import com.nexusblog.exceptions.ProfileNotFoundException;
import com.nexusblog.persistence.entity.Profile;
import com.nexusblog.persistence.entity.VerificationToken;
import com.nexusblog.persistence.repository.ProfileRepository;
import com.nexusblog.persistence.service.interfaces.ProfileService;
import com.nexusblog.persistence.service.interfaces.TokenService;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final TokenService tokenService;
    private final ProfileRepository profileRepository;
    private final ApplicationEventPublisher eventPublisher;

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
    public ProfileDto update(ProfileDto profileDto, MultipartFile file)
            throws ProfileNotFoundException, IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Profile> profileOpt = profileRepository.getByUser_Username(username);

        if (profileOpt.isEmpty()) {
            throw new ProfileNotFoundException("Profile don`t found");
        }
        if (!file.isEmpty()) {
            profileDto.setAvatarPath(updateFile(file, profileDto.getAvatarPath()));
        }

        String newEmail = profileDto.getContacts().getEmail();
        Profile profile = profileOpt.get();
        profileDto.getContacts().setEmail(profile.getContacts().getEmail());

        profileRepository.save(
                ConverterDto.profileFromDto(profile, profileDto));

        if (!newEmail.equals(profile.getContacts().getEmail())) {
            eventPublisher.publishEvent(new OnChangeEmailEvent(profile, newEmail));
        }

        return ConverterDto.profileToDto(profile);
    }

    @Override
    @Transactional
    public void createVerificationToken(Profile profile, String email, String token) {
        Optional<Profile> profileOpt = profileRepository.findById(profile.getId());
        if (profileOpt.isEmpty()) throw new UsernameNotFoundException("Profile don`t exist");

        VerificationToken verToken = new VerificationToken(token, profileOpt.get().getUser());
        verToken.setValueToChange(email);
        tokenService.save(verToken);
    }

    @Override
    public void checkVerificationToken(String token) {
        VerificationToken verToken = tokenService.verify(token);

        Profile profile  = verToken.getUser().getProfile();
        profile.getContacts().setEmail(verToken.getValueToChange());

        profileRepository.save(profile);
        tokenService.delete(verToken);
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
