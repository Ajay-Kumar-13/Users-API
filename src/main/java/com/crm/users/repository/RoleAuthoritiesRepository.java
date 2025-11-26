package com.crm.users.repository;

import com.crm.users.model.RoleAuthorities;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface RoleAuthoritiesRepository extends R2dbcRepository<RoleAuthorities, Void> {

    Flux<RoleAuthorities> findAllByRid(UUID rid);
}
