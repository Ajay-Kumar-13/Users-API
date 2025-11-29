package com.crm.users.repository;

import com.crm.users.model.UserAuthority;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserAuthorityRespository  extends R2dbcRepository<UserAuthority, UUID> {
    Flux<UserAuthority> findAllByUserId(UUID userId);
    Mono<UserAuthority> findByUserIdAndAuthorityId(UUID userId, UUID authorityId);
}
