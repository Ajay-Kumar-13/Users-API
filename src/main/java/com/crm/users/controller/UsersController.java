package com.crm.users.controller;

import com.crm.users.model.User;
import com.crm.users.service.UserService;
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
    public Flux<User> getAllUsers() {
        return userService.getAllUsers();
    }

//    @PostMapping("/newuser")
//    public Mono<User> createUser(@RequestBody User user) {
//        return userService.createUser(user);
//    }
}
