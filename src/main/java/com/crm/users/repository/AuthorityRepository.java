package com.crm.users.repository;

import com.crm.users.model.authorities;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface AuthorityRepository extends R2dbcRepository<authorities, UUID> {
}
