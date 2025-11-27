package com.crm.users.repository;

import com.crm.users.model.Authorities;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthorityRepository extends R2dbcRepository<Authorities, UUID> {

}
