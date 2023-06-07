package com.nexusblog.persistence.repository;

import com.nexusblog.persistence.entity.VerificationToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUser_Username(String username);
}
