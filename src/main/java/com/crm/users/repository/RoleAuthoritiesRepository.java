package com.crm.users.repository;

import com.crm.users.model.RoleAuthorities;
import org.springframework.data.r2dbc.repository.R2dbcRepository;


public interface RoleAuthoritiesRepository extends R2dbcRepository<RoleAuthorities, Void> {
}
