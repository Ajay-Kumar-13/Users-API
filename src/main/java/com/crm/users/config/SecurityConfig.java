package com.crm.users.config;

import com.crm.users.Exception.Exception;
import com.crm.users.Exception.UsersException;
import com.crm.users.repository.RoleRepository;
import com.crm.users.repository.UserRepository;
import com.crm.users.service.security.CustomReactiveAuthenticationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ServerAuthenticationEntryPoint serverAuthenticationEntryPoint;
    private final ServerAccessDeniedHandler serverAccessDeniedHandler;

    public SecurityConfig (
            UserRepository userRepository,
            RoleRepository roleRepository,
            ServerAuthenticationEntryPoint serverAuthenticationEntryPoint,
            ServerAccessDeniedHandler serverAccessDeniedHandler
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.serverAuthenticationEntryPoint = serverAuthenticationEntryPoint;
        this.serverAccessDeniedHandler = serverAccessDeniedHandler;
    }
    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .flatMap(user -> roleRepository.findByRoleId(user.getRole_id())
                        .map(role -> User.withUsername(user.getUsername())
                                .password(user.getPassword())
                                .authorities("ROLE_"+role.getRoleName())
                                .build()
                        ));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        return new CustomReactiveAuthenticationManager(userDetailsService(), passwordEncoder());
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
                        .pathMatchers("/admin/users/newuser").permitAll()
                        .anyExchange().authenticated()
                )
                .authenticationManager(authenticationManager)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

}
