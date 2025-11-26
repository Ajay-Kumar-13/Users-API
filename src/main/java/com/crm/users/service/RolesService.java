package com.crm.users.service;

import com.crm.users.DTO.CreateRoleRequest;
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

    public Flux<Role> getAllRoles() {
        return  roleRepository.findAll();
    }

    public Mono<Role> createRole(CreateRoleRequest createRoleRequest) {

        Role role = new Role();
        role.setRole_name(createRoleRequest.getRole_name());
        try {
           return roleRepository.save(role).flatMap(r ->
                   Flux.fromIterable(createRoleRequest.getAuthorities()).flatMap(auth -> roleAuthorities.save(new RoleAuthorities(r.getRole_id(), auth)))
                           .then(Mono.just(r))
           );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
