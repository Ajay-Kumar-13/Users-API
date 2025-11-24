package com.crm.users.service;

import com.crm.users.model.Role;
import com.crm.users.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolesService {

    private final RoleRepository roleRepository;

    public Flux<Role> getAllRoles() {
        return  roleRepository.findAll();
    }

    public Mono<Role> createRole(Role role) {
        Role newRole = new Role();
        newRole.setRoleName(role.getRoleName());
        List<String> permissions = role.getPermissions().stream().toList();
        newRole.setPermissions(permissions);
        return  roleRepository.save(newRole);
    }
}
