package com.nexusblog.persistence.service.interfaces;

import com.nexusblog.persistence.entity.VerificationToken;

public interface TokenService {
    VerificationToken verify(String token);
    void delete(VerificationToken verificationToken);
    VerificationToken save(VerificationToken verificationToken);
}
