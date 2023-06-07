package com.nexusblog.persistence.service.Impl;

import com.google.common.base.VerifyException;
import com.nexusblog.persistence.entity.VerificationToken;
import com.nexusblog.persistence.repository.TokenRepository;
import com.nexusblog.persistence.service.interfaces.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    @Override
    public VerificationToken verify(String token) {
        Optional<VerificationToken> verTokenOpt = tokenRepository.findByToken(token);
        if (verTokenOpt.isEmpty()) {
            throw new VerifyException("Invalid verification token");
        }

        VerificationToken verToken = verTokenOpt.get();
        Calendar calendar = Calendar.getInstance();
        if (verToken.getExpiryDate().getTime() - calendar.getTime().getTime() <= 0) {
            throw new VerifyException("Time expired");
        }

        return verTokenOpt.get();
    }

    @Override
    public void delete(VerificationToken verificationToken) {
        tokenRepository.delete(verificationToken);
    }

    @Override
    public VerificationToken save(VerificationToken verificationToken) {
        return tokenRepository.save(verificationToken);
    }
}
