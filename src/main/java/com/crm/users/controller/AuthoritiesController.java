package com.crm.users.controller;

import com.crm.users.DTO.CreateAuthorityRequest;
import com.crm.users.DTO.CreateAuthorityResponse;
import com.crm.users.DTO.KeyValuePair;
import com.crm.users.service.AuthoritiesService;
import com.crm.users.util.SystemUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user/admin/authorities")
@PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
public class AuthoritiesController {

    @Autowired
    private AuthoritiesService authoritiesService;

    @Autowired
    private SystemUtils systemUtils;

    @GetMapping
    public Flux<CreateAuthorityResponse> getAllAuthorities() {
        return authoritiesService.getAllAuthorities();
    }

    @PreAuthorize("hasAnyAuthority('AUTHORITY_CREATE', 'CREATE')")
    @PostMapping("")
    public Mono<CreateAuthorityResponse> createAuthority(@Valid @RequestBody CreateAuthorityRequest authority) {
        return authoritiesService.createAuthority(authority);
    }

    @GetMapping("/{roleId}")
    public Mono<List<KeyValuePair>> fetchRoleAuthorities(@PathVariable UUID roleId) {
        return systemUtils.fetchAuthorities(roleId);
    }

    @PreAuthorize("hasAnyAuthority('AUTHORITY_UPDATE', 'UPDATE')")
    @PutMapping("/authId")
    public Mono<CreateAuthorityResponse> updateAuthority(@PathVariable UUID authId, @RequestBody CreateAuthorityRequest authority) {
        return authoritiesService.updateAuthority(authId, authority);
    }

    @PreAuthorize("hasAnyAuthority('AUTHORITY_DELETE', 'DELETE')")
    @DeleteMapping("/authId")
    public Mono<ResponseEntity<String>> deleteAuthority(@PathVariable UUID authId) {
        return authoritiesService.deleteAuthority(authId);
    }
}
