package com.nexusblog.events.listener;

import com.nexusblog.events.event.OnRegistrationCompleteEvent;
import com.nexusblog.persistence.entity.User;
import com.nexusblog.persistence.service.interfaces.EmailService;
import com.nexusblog.persistence.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
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
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);
        String confUrl = String.format("/registrationConfirm?token=%s", token);

        emailService.sendEmailVerifying(user, confUrl);
    }
}
