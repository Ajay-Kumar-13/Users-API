package com.crm.users.controller;

import com.crm.users.model.User;
import com.crm.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class AdminController {

    @Autowired
    UserRepository userRepository;

    @GetMapping()
    public Mono<User> getAllUsers() {
        User user = new User();
        user.setName("AJAY");
        user.setMobile("9346072205");
        return userRepository.save(user);
    }

}
