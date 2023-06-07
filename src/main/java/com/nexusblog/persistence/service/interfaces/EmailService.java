package com.nexusblog.persistence.service.interfaces;

import com.nexusblog.dto.UserDto;

public interface EmailService {
    void sendEmail(String address, String subject, String massage);
    void sendEmailVerifying(UserDto user, String confUrl);
}
