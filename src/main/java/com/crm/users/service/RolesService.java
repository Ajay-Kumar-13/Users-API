package com.crm.users.service;

import com.crm.users.DTO.CreateRoleRequest;
import com.crm.users.DTO.CreateRoleResponse;
import com.crm.users.DTO.OverrideAuthority;
import com.crm.users.DTO.UpdateRoleRequest;
import com.crm.users.Exception.Exception;
import com.crm.users.Exception.RolesException;
import com.crm.users.model.Role;
import com.crm.users.model.RoleAuthorities;
import com.crm.users.repository.RoleAuthoritiesRepository;
import com.crm.users.repository.RoleRepository;
import com.crm.users.util.DatabaseErrorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
        return  roleRepository.findAll().map(role -> new CreateRoleResponse(role.getRoleId(), role.getRoleName(), role.getDescription()))
                .onErrorResume(DatabaseErrorUtil::handleError);
    }

    private Mono<Void> saveRoleAuthorities(Role r, UUID auth) {
        return roleAuthorities.save(new RoleAuthorities(r.getRoleId(), auth))
                .onErrorResume(DatabaseErrorUtil::handleError)
                .then();
    }

    public Mono<CreateRoleResponse> createRole(CreateRoleRequest createRoleRequest) {
        if(createRoleRequest.getRoleName().isBlank()) {
            return Mono.error(new RolesException(Exception.INVALID_ARGUMENTS, new Error("Invalid role name received when creating a role!")));
        }
        Role role = new Role();
        role.setRoleName(createRoleRequest.getRoleName());
        role.setDescription(createRoleRequest.getRoleDesc());
       return roleRepository.save(role).flatMap(r ->
               Flux.fromIterable(createRoleRequest.getAuthorities())
                       .flatMap(auth -> saveRoleAuthorities(r, auth))
                       .then( Mono.just(new CreateRoleResponse(r.getRoleId(), r.getRoleName(), role.getDescription()))))
               .onErrorResume(DatabaseErrorUtil::handleError);
    }

    public Mono<CreateRoleResponse> updateRole(UUID roleId, UpdateRoleRequest updatedRole) {
    return roleRepository.findByRoleId(roleId)
        .flatMap(role -> {
            role.setRoleName(updatedRole.getRoleName());
            role.setDescription(updatedRole.getRoleDesc());
            return roleRepository.save(role);
        })
        .flatMap(savedRole -> Flux.fromIterable(updatedRole.getAuthorities())
            .flatMap(auth -> processAuthority(auth, savedRole, roleId)) 
            .then(Mono.just(savedRole))
        )
        .map(role -> new CreateRoleResponse(role.getRoleId(), role.getRoleName(), role.getDescription()));
    }

    private Mono<Void> processAuthority(OverrideAuthority auth, Role savedRole, UUID roleId) {
        if (auth.isActive()) {
            return saveRoleAuthorities(savedRole, auth.getAuthId());
        } else {
            return rmAuthorityFromRole(auth.getAuthId(), roleId);
        }
    }

    private Mono<Void> rmAuthorityFromRole(UUID authId, UUID roleId) {
        return roleAuthorities.deleteByRidAndAid(roleId, authId);
    }

    public Mono<ResponseEntity<String>> deleteRole(UUID roleId) {
        return roleRepository.deleteByRoleId(roleId)
                .then(Mono.just(ResponseEntity.ok().body("Deleted Successfully!")));
    }
}
