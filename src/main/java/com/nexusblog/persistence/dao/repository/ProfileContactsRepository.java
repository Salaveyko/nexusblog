package com.nexusblog.persistence.dao.repository;

import com.nexusblog.persistence.entity.ProfileContacts;
import org.springframework.data.repository.CrudRepository;

public interface ProfileContactsRepository extends CrudRepository<ProfileContacts, Long> {
}
