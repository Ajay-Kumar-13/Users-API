package com.crm.users.controller;

import com.crm.users.DTO.LoginRequest;
import com.crm.users.DTO.LoginResponse;
import com.crm.users.repository.UserRepository;
import com.crm.users.service.UserService;
import com.crm.users.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/test")
    public Mono<String> test() {
        return Mono.just("Hello world!");
    }

    @PostMapping("/login")
    public Mono<LoginResponse> handleLogin(@RequestBody LoginRequest loginRequest) {
            return userRepository.findByUsername(loginRequest.getUsername())
                    .flatMap(user -> {
                        if(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                            return userService.getUserByUsername(user.getUsername()).flatMap(savedUser -> Mono.just(new LoginResponse(jwtUtils.generateJwtFromUsername(savedUser))));
                        } else {
                            return Mono.empty();
                        }
                    });
    }
}
