package com.nexusblog.controllers;

import com.nexusblog.dto.ProfileDto;
import com.nexusblog.exceptions.ProfileNotFoundException;
import com.nexusblog.persistence.service.interfaces.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RequiredArgsConstructor
@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping
    public String profile() {
        return "redirect:profile/" + SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping("/{username}")
    public String profile(@PathVariable String username, Model model) throws ProfileNotFoundException {
        model.addAttribute("profile", profileService.get(username));
        return "profile";
    }

    @PostMapping
    public String update(@RequestParam("file") MultipartFile file,
                         @ModelAttribute("profile") @Valid ProfileDto profile,
                         BindingResult result,
                         Model model) throws ProfileNotFoundException, IOException {

        if (result.hasErrors()) {
            model.addAttribute("profile", profile);
            return "";
        }

        profileService.update(profile, file);
        return "profile";
    }

    @GetMapping("emailConfirm")
    public String confirmEmail(@RequestParam("token") String token){
        profileService.checkVerificationToken(token);

        return "redirect:/profile";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
