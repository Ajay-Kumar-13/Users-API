package com.crm.users.repository;

import com.crm.users.model.RefreshToken;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RefreshTokenRepository extends R2dbcRepository<RefreshToken, UUID> {
    Mono<RefreshToken> findByToken(String token);
    Mono<Void> deleteByUserId(UUID userId);
    Mono<Void> deleteByToken(String token);
}
