package com.crm.users.controller;

import com.crm.users.DTO.CreateRoleRequest;
import com.crm.users.DTO.CreateRoleResponse;
import com.crm.users.DTO.KeyValuePair;
import com.crm.users.model.Role;
import com.crm.users.service.RolesService;
import com.crm.users.util.SystemUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/admin/roles")
@PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
public class RolesController {

    private final RolesService rolesService;
    private final SystemUtils systemUtils;

    @PreAuthorize("hasAnyAuthority('ROLE_READ', 'READ')")
    @GetMapping
    public Flux<CreateRoleResponse> getAllRoles() {
        return rolesService.getAllRoles();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CREATE', 'CREATE')")
    @PostMapping("")
    public Mono<CreateRoleResponse> createRole(@Valid @RequestBody CreateRoleRequest role) {
        return rolesService.createRole(role);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CREATE', 'CREATE')")
    @GetMapping("/{roleId}")
    public Mono<KeyValuePair> fetchRole(@PathVariable UUID roleId) {
        return systemUtils.fetchRole(roleId).flatMap(role -> Mono.just(new KeyValuePair(role.getRoleId(), role.getRoleName(), role.getDescription())));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_UPDATE', 'UPDATE')")
    @PutMapping("/{roleId}")
    public Mono<CreateRoleResponse> updateRole(@PathVariable UUID roleId, @RequestBody CreateRoleRequest role) {
        return rolesService.updateRole(roleId, role);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_DELETE', 'DELETE')")
    @DeleteMapping("/{roleId}")
    public Mono<ResponseEntity<String>> deleteRole(@PathVariable UUID roleId) {
        return rolesService.deleteRole(roleId);
    }
}
