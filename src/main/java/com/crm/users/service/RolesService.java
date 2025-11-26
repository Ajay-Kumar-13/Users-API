package com.crm.users.service;

import com.crm.users.DTO.CreateRoleRequest;
import com.crm.users.DTO.CreateRoleResponse;
import com.crm.users.model.Role;
import com.crm.users.model.RoleAuthorities;
import com.crm.users.repository.RoleAuthoritiesRepository;
import com.crm.users.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RolesService {

    private final RoleRepository roleRepository;
    private final RoleAuthoritiesRepository roleAuthorities;

    public Flux<CreateRoleResponse> getAllRoles() {
        return  roleRepository.findAll().map(role -> new CreateRoleResponse(role.getRoleId(), role.getRoleName()));
    }

    public Mono<CreateRoleResponse> createRole(CreateRoleRequest createRoleRequest) {

        Role role = new Role();
        role.setRoleName(createRoleRequest.getRoleName());
        try {
           return roleRepository.save(role).flatMap(r ->
                   Flux.fromIterable(createRoleRequest.getAuthorities())
                           .flatMap(auth -> roleAuthorities.save(new RoleAuthorities(r.getRoleId(), auth)))
                           .then( Mono.just(new CreateRoleResponse(r.getRoleId(), r.getRoleName())))

           );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
