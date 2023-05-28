package com.nexusblog.controllers;

import com.nexusblog.dto.UserDto;
import com.nexusblog.persistence.dao.service.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String regForm(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);

        return "/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") @Valid UserDto userDto,
                               BindingResult result,
                               Model model) {

        boolean existUser = true;
        try {
            userService.findByUsername(userDto.getUsername());
        } catch (UsernameNotFoundException e){
            existUser = false;
        }

        if (existUser) {
            result.rejectValue("username", null,
                    "User already exists");
        }
        if (!userDto.getPassword().equals(userDto.getPasswordConfirm())) {
            result.rejectValue("password", null,
                    "Passwords don`t match");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/registration";
        }

        userService.saveUser(userDto);
        return "redirect:/blog";
    }
}
