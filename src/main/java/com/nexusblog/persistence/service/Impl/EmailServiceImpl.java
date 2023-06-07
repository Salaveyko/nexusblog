package com.nexusblog.persistence.service.Impl;

import com.nexusblog.persistence.entity.User;
import com.nexusblog.persistence.service.interfaces.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {
    @Value("${spring.mail.username}")
    private String username;
    private final JavaMailSender mailSender;

    public void sendEmail(String address, String subject, String massage){
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo(address);
        mailMessage.setSubject(subject);
        mailMessage.setText(massage);

        mailSender.send(mailMessage);
    }

    @Override
    public void sendEmailVerifying(User user, String confUrl) {
        String recipientAddress = user.getProfile().getContacts().getEmail();
        String subject = "Registration Confirmation";
        String message = "Hello.\n";
        String content = String.format("%s\r\nhttp://localhost:8080%s", message, confUrl);

        sendEmail(recipientAddress, subject, content);
    }

    @Override
    public void sendEmailChangesVerifying(String email, String confUrl) {
        String subject = "Email Confirmation";
        String message = "Hello.\n";
        String content = String.format("%s\r\nhttp://localhost:8080%s", message, confUrl);

        sendEmail(email, subject, content);
    }
}
