package com.crm.users.controller;

import com.crm.users.DTO.CreateUserRequest;
import com.crm.users.DTO.CreateUserResponse;
import com.crm.users.model.User;
import com.crm.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/admin/users")
public class UsersController {

    @Autowired
    UserService userService;

    @GetMapping()
    public Flux<CreateUserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/newuser")
    public Mono<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest user) {
        return userService.createUser(user);
    }
}
