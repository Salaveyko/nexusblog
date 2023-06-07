package com.nexusblog.controllers;

import com.nexusblog.dto.UserDto;
import com.nexusblog.persistence.service.interfaces.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

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
                               HttpServletRequest request,
                               Model model) {

        boolean existUser = true;
        try {
            userService.findByUsername(userDto.getUsername());
        } catch (UsernameNotFoundException e) {
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

        userService.saveNewUser(userDto);

        return "redirect:/login";
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {

        userService.checkVerificationToken(token, request, response);

        return "redirect:/";
    }
}
