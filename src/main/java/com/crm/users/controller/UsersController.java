package com.crm.users.controller;

import com.crm.users.DTO.CreateUserAuthorities;
import com.crm.users.DTO.CreateUserRequest;
import com.crm.users.DTO.CreateUserResponse;
import com.crm.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class UsersController {

    @Autowired
    UserService userService;

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping()
    public Flux<CreateUserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/newuser")
    public Mono<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest user) {
        return userService.createUser(user);
    }

    @PostMapping("/{userId}/override-permissions")
    public Mono<CreateUserResponse> overridePermissions(
            @Valid
            @RequestBody CreateUserAuthorities userAuthorities,
            @PathVariable UUID userId
    ) {
        return userService.overridePermissions(userAuthorities, userId);
    }
}
