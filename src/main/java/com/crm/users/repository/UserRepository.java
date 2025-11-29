package com.crm.users.repository;

import com.crm.users.model.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {
    Mono<User> findById(UUID id);
}
