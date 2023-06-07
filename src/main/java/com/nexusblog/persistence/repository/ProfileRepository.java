package com.nexusblog.persistence.repository;

import com.nexusblog.persistence.entity.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends CrudRepository<Profile, Long> {
    Optional<Profile> getByUser_Username(String username);
}
