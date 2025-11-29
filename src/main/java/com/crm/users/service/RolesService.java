package com.crm.users.service;

import com.crm.users.DTO.CreateRoleRequest;
import com.crm.users.DTO.CreateRoleResponse;
import com.crm.users.Exception.Exception;
import com.crm.users.Exception.RoleAuthoritiesException;
import com.crm.users.Exception.RolesException;
import com.crm.users.model.Role;
import com.crm.users.model.RoleAuthorities;
import com.crm.users.repository.RoleAuthoritiesRepository;
import com.crm.users.repository.RoleRepository;
import com.crm.users.util.DatabaseErrorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RolesService {

    private final RoleRepository roleRepository;
    private final RoleAuthoritiesRepository roleAuthorities;

    public Flux<CreateRoleResponse> getAllRoles() {
        return  roleRepository.findAll().map(role -> new CreateRoleResponse(role.getRoleId(), role.getRoleName()))
                .onErrorResume(DatabaseErrorUtil::handleError);
    }

    private Mono<RoleAuthorities> saveRoleAuthorities(Role r, UUID auth) {
        return roleAuthorities.save(new RoleAuthorities(r.getRoleId(), auth))
                .onErrorResume(DatabaseErrorUtil::handleError);
    }

    public Mono<CreateRoleResponse> createRole(CreateRoleRequest createRoleRequest) {
        if(createRoleRequest.getRoleName().isBlank()) {
            return Mono.error(new RolesException(Exception.INVALID_ARGUMENTS, new Error("Invalid role name received when creating a role!")));
        }
        Role role = new Role();
        role.setRoleName(createRoleRequest.getRoleName());
       return roleRepository.save(role).flatMap(r ->
               Flux.fromIterable(createRoleRequest.getAuthorities())
                       .flatMap(auth -> saveRoleAuthorities(r, auth))
                       .then( Mono.just(new CreateRoleResponse(r.getRoleId(), r.getRoleName()))))
               .onErrorResume(DatabaseErrorUtil::handleError);
    }

}
