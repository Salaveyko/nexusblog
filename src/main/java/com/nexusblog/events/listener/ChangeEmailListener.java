package com.nexusblog.events.listener;

import com.nexusblog.events.event.OnChangeEmailEvent;
import com.nexusblog.exceptions.ProfileNotFoundException;
import com.nexusblog.persistence.entity.Profile;
import com.nexusblog.persistence.service.interfaces.EmailService;
import com.nexusblog.persistence.service.interfaces.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class ChangeEmailListener implements ApplicationListener<OnChangeEmailEvent> {
    private final ProfileService profileService;
    private final EmailService emailService;

    @Override
    public void onApplicationEvent(OnChangeEmailEvent event) {
        confirmChangeEmail(event);
    }

    private void confirmChangeEmail(OnChangeEmailEvent event) {
        String email = event.getEmail();
        String token = UUID.randomUUID().toString();
        profileService.createVerificationToken(event.getProfile(), email, token);
        String confirmUrl = String.format("/profile/emailConfirm?token=%s", token);

        emailService.sendEmailChangesVerifying(email, confirmUrl);
    }
}
