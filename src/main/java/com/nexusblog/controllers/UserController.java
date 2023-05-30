package com.nexusblog.controllers;

import com.nexusblog.exceptions.ProfileNotFoundException;
import com.nexusblog.persistence.dao.service.interfaces.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@Controller
@RequestMapping("/profile")
public class UserController {

    private final ProfileService profileService;

    @Autowired
    public UserController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("")
    public String profile(Model model) throws ProfileNotFoundException {
        model.addAttribute("profile", profileService.get());

        return "profile.html";
    }

    @GetMapping("/{username}")
    public String profile(@PathVariable String username, Model model) throws ProfileNotFoundException {
        model.addAttribute("profile", profileService.get(username));

        return "profile.html";
    }
}
