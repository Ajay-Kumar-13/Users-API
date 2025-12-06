package com.crm.users.config;

import com.crm.users.security.CustomReactiveAuthenticationManager;
import com.crm.users.security.JwtSecurityContextRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final ServerAuthenticationEntryPoint serverAuthenticationEntryPoint;
    private final ServerAccessDeniedHandler serverAccessDeniedHandler;
    private final JwtSecurityContextRepository jwtSecurityContextRepository;

    public SecurityConfig (
            ServerAuthenticationEntryPoint serverAuthenticationEntryPoint,
            ServerAccessDeniedHandler serverAccessDeniedHandler,
            JwtSecurityContextRepository jwtSecurityContextRepository
    ) {
        this.serverAuthenticationEntryPoint = serverAuthenticationEntryPoint;
        this.serverAccessDeniedHandler = serverAccessDeniedHandler;
        this.jwtSecurityContextRepository = jwtSecurityContextRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        return new CustomReactiveAuthenticationManager();
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(
            ServerHttpSecurity http,
            ReactiveAuthenticationManager authenticationManager
    ) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(serverAuthenticationEntryPoint)
                                .accessDeniedHandler(serverAccessDeniedHandler)
                )
                .authorizeExchange(auth -> auth
                        .pathMatchers("/auth/**").permitAll()
                        .anyExchange().authenticated()
                )
                .authenticationManager(authenticationManager)
                .securityContextRepository(jwtSecurityContextRepository)
                .httpBasic(Customizer.withDefaults())
                .build();
    }

}
