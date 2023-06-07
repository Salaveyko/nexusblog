package com.nexusblog.events.listener;

import com.nexusblog.dto.UserDto;
import com.nexusblog.events.event.OnRegistrationCompleteEvent;
import com.nexusblog.persistence.service.interfaces.EmailService;
import com.nexusblog.persistence.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    private final UserService userService;
    private final EmailService emailService;


    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        UserDto user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);
        String confUrl = String.format("%s/registrationConfirm?token=%s", event.getAppUrl(), token);

        emailService.sendEmailVerifying(user, confUrl);
    }
}
