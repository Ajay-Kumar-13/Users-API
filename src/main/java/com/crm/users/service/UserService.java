package com.crm.users.service;

import com.crm.users.DTO.CreateUserRequest;
import com.crm.users.DTO.CreateUserResponse;
import com.crm.users.DTO.KeyValuePair;
import com.crm.users.Exception.*;
import com.crm.users.Exception.Exception;
import com.crm.users.model.Role;
import com.crm.users.model.RoleAuthorities;
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
        }))
        .onErrorResume(err -> {
            if (err instanceof RolesException) {
                return Flux.error(err);
            } else if(err instanceof AuthoritiesException) {
                return Flux.error(err);
            }
            return Flux.error(new UsersException(Exception.USERS_EXCEPTION, err));
        });
    }

    private Mono<Role> fetchRole(UUID roleId) {
        return roleRepository.findByRoleId(roleId)
                 .onErrorResume(err -> Mono.error(new RolesException(Exception.ROLES_EXCEPTION, err)));
    }

    private Flux<RoleAuthorities> fetchRoleAuthorities(UUID roleId) {
        return roleAuthoritiesRepository.findAllByRid(roleId)
                .onErrorResume(err -> Mono.error(new RoleAuthoritiesException(Exception.ROLE_AUTHORITIES_EXCEPTION, err)));
    }

    private Mono<List<KeyValuePair>> fetchAuthorities(UUID roleId) {
        return fetchRoleAuthorities(roleId).flatMap(roleAuthority ->
                authorityRepository.findById(roleAuthority.getAid()).map(authority ->
                        new KeyValuePair(authority.getAuthorityId(), authority.getAuthorityName().name())))
                .collectList()
                .onErrorResume(err -> {
                    if(err instanceof RoleAuthoritiesException) {
                        return Mono.error(err);
                    }
                    return Mono.error(new AuthoritiesException(Exception.AUTHORITIES_EXCEPTION, err));
                });
    }

    public Mono<CreateUserResponse> createUser(@RequestBody CreateUserRequest user) {
        if(user.getUsername().isBlank()) {
            return Mono.error(new UsersException(Exception.INVALID_ARGUMENTS, new Error("Invalid username received when creating an user!")));
        }
        if(user.getRoleId().toString().isBlank()) {
            return Mono.error(new UsersException(Exception.INVALID_ARGUMENTS, new Error("Invalid role id received when creating an user!")));
        }
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
        .onErrorResume(err -> {
            if (err instanceof RolesException) {
                return Mono.error(err);
            }
            if (err instanceof AuthoritiesException) {
                    return Mono.error(err);
            }
            return Mono.error(new UsersException(Exception.USERS_EXCEPTION, err));
        });
    }
}
