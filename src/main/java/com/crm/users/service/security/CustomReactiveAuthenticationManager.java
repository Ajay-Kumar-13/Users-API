package com.crm.users.service.security;

import com.crm.users.Exception.Exception;
import com.crm.users.Exception.UsersException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final ReactiveUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public CustomReactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String username = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        return userDetailsService.findByUsername(username)
                .switchIfEmpty(Mono.error(new UsersException(Exception.AUTHENTICATION_FAILED, new RuntimeException("Authentication Failed: USER NOT FOUND"))))
                .flatMap(user -> {
                    if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
                        return Mono.error(new UsersException(Exception.AUTHENTICATION_FAILED, new RuntimeException("Authentication Failed: Invalid Credentials")));
                    }
                    return Mono.just(
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    rawPassword,
                                    user.getAuthorities()
                            ));
                });
    }
}
