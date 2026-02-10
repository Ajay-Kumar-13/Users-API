package com.crm.users.controller;

import com.crm.users.DTO.CreateRoleRequest;
import com.crm.users.DTO.CreateRoleResponse;
import com.crm.users.model.Role;
import com.crm.users.service.RolesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/roles")
@PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
public class RolesController {

    private final RolesService rolesService;

    @PreAuthorize("hasAnyAuthority('ROLE_READ', 'READ')")
    @GetMapping
    public Flux<CreateRoleResponse> getAllRoles() {
        return rolesService.getAllRoles();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CREATE', 'CREATE')")
    @PostMapping("/newrole")
    public Mono<CreateRoleResponse> createRole(@Valid @RequestBody CreateRoleRequest role) {
        return rolesService.createRole(role);
    }

}
