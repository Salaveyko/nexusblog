package com.nexusblog.persistence.repository;

import com.nexusblog.persistence.entity.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}
