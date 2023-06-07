package com.nexusblog.persistence.service.interfaces;

import com.nexusblog.persistence.entity.Profile;
import com.nexusblog.persistence.entity.User;

public interface EmailService {
    void sendEmail(String address, String subject, String massage);
    void sendEmailVerifying(User user, String confirmUrl);

    void sendEmailChangesVerifying(String email, String confirmUrl);
}
