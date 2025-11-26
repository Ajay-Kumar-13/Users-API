package com.crm.users.controller;

import com.crm.users.DTO.CreateAuthorityRequest;
import com.crm.users.DTO.CreateAuthorityResponse;
import com.crm.users.model.Authorities;
import com.crm.users.service.AuthoritiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/admin/authorities")
public class AuthoritiesController {

    @Autowired
    private AuthoritiesService authoritiesService;

    @GetMapping
    public Flux<CreateAuthorityResponse> getAllAuthorities() {
        return authoritiesService.getAllAuthorities();
    }

    @PostMapping("/new-authority")
    public Mono<CreateAuthorityResponse> createAuthority(@RequestBody CreateAuthorityRequest authority) {
        return authoritiesService.createAuthority(authority);
    }
}
