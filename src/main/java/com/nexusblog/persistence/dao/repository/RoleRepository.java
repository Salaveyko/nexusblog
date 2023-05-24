package com.nexusblog.persistence.dao.repository;

import com.nexusblog.persistence.entity.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByName(String name);
}
