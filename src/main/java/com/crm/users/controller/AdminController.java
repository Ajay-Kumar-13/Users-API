package com.crm.users.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class AdminController {

    @GetMapping("/")
    public String getAllUsers() {
        return "This Endpoint will return all the users";
    }

}
