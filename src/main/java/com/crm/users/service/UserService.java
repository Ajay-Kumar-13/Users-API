package com.crm.users.service;

import com.crm.users.DTO.CreateUserRequest;
import com.crm.users.DTO.CreateUserResponse;
import com.crm.users.DTO.KeyValuePair;
import com.crm.users.model.Role;
import com.crm.users.model.User;
import com.crm.users.repository.AuthorityRepository;
import com.crm.users.repository.RoleAuthoritiesRepository;
import com.crm.users.repository.RoleRepository;
import com.crm.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

   private final UserRepository userRepository;
   private final RoleRepository roleRepository;
   private final RoleAuthoritiesRepository roleAuthoritiesRepository;
   private final AuthorityRepository authorityRepository;

    public Flux<CreateUserResponse> getAllUsers() {
        return userRepository.findAll().flatMap(user ->
            fetchRole(user.getRole_id()).flatMap(role -> {
                KeyValuePair userRole = new KeyValuePair(role.getRoleId(), role.getRoleName());
                return fetchAuthorities(role.getRoleId()).map(roleAuthorities ->
                        new CreateUserResponse(user.getId(), user.getName(), userRole, roleAuthorities));
        }));
    }

    private Mono<Role> fetchRole(UUID roleId) {
        return roleRepository.findByRoleId(roleId);
    }


    private Mono<List<KeyValuePair>> fetchAuthorities(UUID roleId) {
        return roleAuthoritiesRepository.findAllByRid(roleId).flatMap(roleAuthority ->
                authorityRepository.findById(roleAuthority.getAid()).map(authority ->
                        new KeyValuePair(authority.getAuthorityId(), authority.getAuthorityName().name())))
                .collectList();
    }

    public Mono<CreateUserResponse> createUser(@RequestBody CreateUserRequest user) {
        User newUser = new User();
        newUser.setName(user.getUsername());
        return fetchRole(user.getRoleId()).flatMap(role -> {
            newUser.setRole_id(role.getRoleId());
            return userRepository.save(newUser).flatMap(savedUser -> {
                KeyValuePair userRole = new KeyValuePair(role.getRoleId(), role.getRoleName());
                return fetchAuthorities(role.getRoleId()).flatMap(roleAuthorities ->
                        Mono.just(new CreateUserResponse(savedUser.getId(), savedUser.getName(), userRole, roleAuthorities)));
            });
        })
        .switchIfEmpty(Mono.error(new IllegalArgumentException("Role not found")));
    }
}
