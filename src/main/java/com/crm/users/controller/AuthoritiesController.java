package com.crm.users.controller;

import com.crm.users.DTO.CreateAuthorityRequest;
import com.crm.users.DTO.CreateAuthorityResponse;
import com.crm.users.DTO.KeyValuePair;
import com.crm.users.service.AuthoritiesService;
import com.crm.users.util.SystemUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/authorities")
@PreAuthorize("hasRole('ROOT')")
public class AuthoritiesController {

    @Autowired
    private AuthoritiesService authoritiesService;

    @Autowired
    private SystemUtils systemUtils;

    @GetMapping
    public Flux<CreateAuthorityResponse> getAllAuthorities() {
        return authoritiesService.getAllAuthorities();
    }

    @PostMapping("")
    public Mono<CreateAuthorityResponse> createAuthority(@Valid @RequestBody CreateAuthorityRequest authority) {
        return authoritiesService.createAuthority(authority);
    }

    @GetMapping("/{roleId}")
    public Mono<List<KeyValuePair>> fetchRoleAuthorities(@PathVariable UUID roleId) {
        return systemUtils.fetchAuthorities(roleId);
    }
}
