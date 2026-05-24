package com.crm.users.controller;

import com.crm.users.DTO.CreateUserResponse;
import com.crm.users.DTO.LoginRequest;
import com.crm.users.DTO.LoginResponse;
import com.crm.users.DTO.RefreshToken;
import com.crm.users.repository.UserRepository;
import com.crm.users.service.RefreshTokenService;
import com.crm.users.service.UserService;
import com.crm.users.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/api/user/auth")
public class AuthController {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RefreshTokenService refreshTokenService;

    @GetMapping("/test")
    public Mono<String> test() {
        return Mono.just("Hello world!");
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponse>> handleLogin(@RequestBody LoginRequest loginRequest) {
        // Use generic error message to prevent username enumeration
        final String GENERIC_ERROR = "Authentication failed. Invalid username or password.";
        
        return userRepository.findByUsername(loginRequest.getUsername())
                .switchIfEmpty(Mono.error(new RuntimeException(GENERIC_ERROR)))
                .flatMap(user -> {
                    if(passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())) {
                             return userService.getUserByUsername(user.getUsername())
                                     .flatMap(savedUser ->
                                             refreshTokenService.createRefreshToken(savedUser.getId())
                                                 .map(refreshToken -> {
                                                     ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken.getRefreshToken())
                                                             .httpOnly(true)
                                                             .secure(true)
                                                             .path("/api/user/auth")
                                                             .maxAge(Duration.ofDays(1))
                                                             .build();

                                                     return ResponseEntity.ok()
                                                             .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                                             .body(new LoginResponse(jwtUtils.generateJwtFromUsername(savedUser)));
                                                 }));
                    }
                    return Mono.error(new RuntimeException(GENERIC_ERROR));
                });
    }

    @PostMapping("/refresh")
    public Mono<ResponseEntity<LoginResponse>> refresh(@CookieValue("refreshToken") String token) {
        return refreshTokenService.validateRefreshToken(token)
                .flatMap(refreshToken -> refreshTokenService.createRefreshToken(refreshToken.getUser().getId())
                        .flatMap(newRefreshToken -> {
                            CreateUserResponse user = newRefreshToken.getUser();
                            String accessToken = jwtUtils.generateJwtFromUsername(user);
                            return refreshTokenService.deleteRefreshToken(refreshToken.getRefreshToken())
                                    .flatMap(responseEntity -> {
                                        if(responseEntity.hasBody()) {
                                            ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken.getRefreshToken())
                                                .httpOnly(true)
                                                .path("/api/user/auth")
                                                .maxAge(Duration.ofDays(1))
                                                .build();
                                            // Return a response entity and udpate the refreshToken cookie value.
                                            return Mono.just(ResponseEntity.ok()
                                                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                                .body(new LoginResponse(accessToken)));
                                        }
                                        return Mono.error(new RuntimeException("Invalid Refresh Token!"));
                                    });
                        }));
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<?>> logout(@CookieValue("refreshToken") String token) {
        return refreshTokenService.deleteRefreshToken(token)
                .flatMap(responseEntity -> {
                   if(responseEntity.hasBody()) {
                       return Mono.just(ResponseEntity.ok().body("Logged Out Successfully!"));
                   }
                   return Mono.error(new RuntimeException("Some issue has been encountered while logging out!"));
                });
    }
}
