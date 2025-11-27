package com.crm.users.repository;

import com.crm.users.model.Role;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface RoleRepository extends R2dbcRepository<Role, UUID> {
    Mono<Role> findByRoleId(UUID roleId);
}
