package com.nexusblog.controllers;

import com.nexusblog.dto.ProfileDto;
import com.nexusblog.exceptions.ProfileNotFoundException;
import com.nexusblog.persistence.dao.service.interfaces.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Value("${upload.path}")
    private String uploadPath;
    private final ProfileService profileService;

    @GetMapping("/{username}")
    public String profile(@PathVariable String username, Model model) throws ProfileNotFoundException {
        model.addAttribute("profile", profileService.get(username));
        return "profile";
    }

    @PostMapping("")
    public String update(@RequestParam("file") MultipartFile file,
                         @ModelAttribute("profile") @Valid ProfileDto profile,
                         BindingResult result,
                         Model model) throws ProfileNotFoundException, IOException {
        if (result.hasErrors()) {
            model.addAttribute("profile", profile);
            return "";
        }

        if(file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()){
            File uploadDir = new File(uploadPath + "/avatar");
            if(!uploadDir.exists()){
                uploadDir.mkdirs();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            File outputFile = new File(uploadDir + "/" + resultFilename);

            Thumbnails.of(file.getInputStream())
                    .size(100, 100)
                    .outputFormat("jpg")
                    .toFile(outputFile);

            profile.setAvatarPath(resultFilename);
        }

        profileService.update(profile);
        return "profile";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
