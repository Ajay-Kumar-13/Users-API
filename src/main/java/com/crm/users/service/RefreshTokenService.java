package com.crm.users.service;

import com.crm.users.DTO.RefreshToken;
import com.crm.users.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    public Mono<RefreshToken> createRefreshToken(UUID user) {
        com.crm.users.model.RefreshToken token = new com.crm.users.model.RefreshToken();
        token.setUserId(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiresAt(Instant.now().plus(1, ChronoUnit.DAYS));

        return refreshTokenRepository.save(token).flatMap(savedRefreshToken ->
                userService.getUserById(user)
                        .flatMap(savedUser -> {
                            return Mono.just(new RefreshToken(savedRefreshToken.getToken(), savedUser));
                        })
        );
    }

    public Mono<RefreshToken> validateRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .switchIfEmpty(Mono.error(new RuntimeException("Token is Invalid!")))
                .flatMap(refreshToken -> {
                    if(refreshToken.getExpiresAt().isBefore(Instant.now())) {
                        return Mono.error(new RuntimeException("Token is Expired"));
                    }
                    return userService.getUserById(refreshToken.getUserId())
                            .flatMap(savedUser -> {
                                return Mono.just(new RefreshToken(refreshToken.getToken(), savedUser));
                            });
                });
    }

    public Mono<ResponseEntity<?>> deleteRefreshTokenByUser(UUID user) {
        return refreshTokenRepository.deleteByUserId(user)
                .then(Mono.just(ResponseEntity.ok("All Refresh Tokens got Deleted Successfully!")));
    }

    public Mono<ResponseEntity<?>> deleteRefreshToken(String token) {
        return refreshTokenRepository.deleteByToken(token)
                .then(Mono.just(ResponseEntity.ok("Refresh token deleted successfully!")));
    }

}
