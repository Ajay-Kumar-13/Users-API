package com.crm.users.controller;

import com.crm.users.DTO.CreateRoleRequest;
import com.crm.users.model.Role;
import com.crm.users.service.RolesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/roles")
public class RolesController {

    private final RolesService rolesService;

    @GetMapping
    public Flux<Role> getAllRoles() {
        return rolesService.getAllRoles();
    }

    @PostMapping("/newrole")
    public Mono<Role> createRole(@RequestBody CreateRoleRequest role) {
        return rolesService.createRole(role);
    }

}
