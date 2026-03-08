package com.crm.users.service;

import com.crm.users.DTO.*;
import com.crm.users.Exception.*;
import com.crm.users.Exception.Exception;
import com.crm.users.model.*;
import com.crm.users.repository.*;
import com.crm.users.util.DatabaseErrorUtil;
import com.crm.users.util.SystemUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

   private final UserRepository userRepository;
   private final RoleRepository roleRepository;
   private final RoleAuthoritiesRepository roleAuthoritiesRepository;
   private final AuthorityRepository authorityRepository;
   private final UserAuthorityRespository userAuthorityRespository;
   private final SystemUtils systemUtils;

   private final AuthoritiesService authoritiesService;

   private final PasswordEncoder passwordEncoder;

    public Flux<CreateUserResponse> getAllUsers() {
        return userRepository.findAll().flatMap(user ->
            systemUtils.fetchRole(user.getRole_id()).flatMap(role -> {
                KeyValuePair userRole = new KeyValuePair(role.getRoleId(), role.getRoleName());
                return systemUtils.fetchAuthorities(role.getRoleId())
                        .flatMap(keyValuePairs -> overrideAuthorities(keyValuePairs,  user.getId()))
                        .map(roleAuthorities ->
                        new CreateUserResponse(user.getId(), user.getUsername(), user.getEmail(), userRole, roleAuthorities, user.is_account_active()));
        }))
        .onErrorResume(DatabaseErrorUtil::handleError);
    }

    private Mono<List<KeyValuePair>> overrideAuthorities( List<KeyValuePair> roleAuthorities, UUID userId) {
        List<KeyValuePair> updatedRoleAuthorities = new ArrayList<>(roleAuthorities);
        return userAuthorityRespository.findAllByUserId(userId).flatMap(userAuthority -> {
            boolean hasAuthority = roleAuthorities.stream().anyMatch(ra -> userAuthority.getAuthorityId().equals(ra.getId()));
            if(!hasAuthority && userAuthority.isActive()) {
                return authoritiesService.fetchAuthorityById(userAuthority.getAuthorityId()).map(authority -> {
                    updatedRoleAuthorities.add(new KeyValuePair(authority.getAuthorityId(), authority.getAuthorityName()));
                    return authority;
                });
            } else if(hasAuthority && !userAuthority.isActive()) {
                return authoritiesService.fetchAuthorityById(userAuthority.getAuthorityId()).map(authority -> {
                    updatedRoleAuthorities.remove(new KeyValuePair(authority.getAuthorityId(), authority.getAuthorityName()));
                    return authority;
                });
            }
            return Mono.just(userAuthority);
        })
        .onErrorResume(DatabaseErrorUtil::handleError)
        .then(Mono.fromCallable(() -> updatedRoleAuthorities));
    }

    public Mono<CreateUserResponse> createUser(CreateUserRequest user) {
        if(user.getUsername().isBlank()) {
            return Mono.error(new UsersException(Exception.INVALID_ARGUMENTS, new Error("Invalid username received when creating an user!")));
        }
        if(user.getRoleId().toString().isBlank()) {
            return Mono.error(new UsersException(Exception.INVALID_ARGUMENTS, new Error("Invalid role id received when creating an user!")));
        }
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.set_account_active(user.isAccountActive());
        return systemUtils.fetchRole(user.getRoleId()).flatMap(role -> {
            newUser.setRole_id(role.getRoleId());
            return userRepository.save(newUser).flatMap(savedUser -> {
                KeyValuePair userRole = new KeyValuePair(role.getRoleId(), role.getRoleName());
                return systemUtils.fetchAuthorities(role.getRoleId()).flatMap(roleAuthorities ->
                        Mono.just(new CreateUserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), userRole, roleAuthorities, savedUser.is_account_active())));
            });
        })
        .onErrorResume(DatabaseErrorUtil::handleError);
    }

    public Mono<CreateUserResponse> getUserById(UUID userId) {
        return userRepository.findById(userId).flatMap(savedUser ->
                systemUtils.fetchRole(savedUser.getRole_id()).flatMap(role -> {
            KeyValuePair userRole = new KeyValuePair(role.getRoleId(), role.getRoleName());
            return systemUtils.fetchAuthorities(role.getRoleId())
                    .flatMap(keyValuePairs -> overrideAuthorities(keyValuePairs,  savedUser.getId()))
                    .flatMap(roleAuthorities ->
                    Mono.just(new CreateUserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), userRole, roleAuthorities, savedUser.is_account_active())));
        }))
        .onErrorResume(DatabaseErrorUtil::handleError);
    }

    public Mono<CreateUserResponse> getUserByUsername(String username) {
        return userRepository.findByUsername(username).flatMap(savedUser ->
                        systemUtils.fetchRole(savedUser.getRole_id()).flatMap(role -> {
                            KeyValuePair userRole = new KeyValuePair(role.getRoleId(), role.getRoleName());
                            return systemUtils.fetchAuthorities(role.getRoleId())
                                    .flatMap(keyValuePairs -> overrideAuthorities(keyValuePairs,  savedUser.getId()))
                                    .flatMap(roleAuthorities ->
                                    Mono.just(new CreateUserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), userRole, roleAuthorities, savedUser.is_account_active())));
                        }))
                .onErrorResume(DatabaseErrorUtil::handleError);
    }

    public Mono<CreateUserResponse> overridePermissions(CreateUserAuthorities userAuthorities, UUID userId){
        return getUserById(userId).flatMap(user -> {
            boolean hasAuthority = user.getAuthorities().stream().anyMatch(kv -> kv.getName().equalsIgnoreCase(userAuthorities.getAuthorityName().name().trim()));

            if(hasAuthority) {
                KeyValuePair authority = user.getAuthorities().stream().filter(keyValuePair -> keyValuePair.getName().equalsIgnoreCase(userAuthorities.getAuthorityName().name().trim())).findFirst().orElse(null);
                return userAuthorityRespository.findByUserIdAndAuthorityId(userId, authority.getId())
                        .defaultIfEmpty(new UserAuthority(userId, authority.getId(), userAuthorities.isActive()))
                        .flatMap(exitingUser -> {
                           exitingUser.setActive(userAuthorities.isActive());
                           return userAuthorityRespository.save(exitingUser);
                        })
                        .flatMap(savedUser -> getUserById(savedUser.getUserId()));
            } else {
                return authoritiesService.fetchAuthorityByName(Authority.valueOf(userAuthorities.getAuthorityName().name())).flatMap(authority ->
                                userAuthorityRespository.findByUserIdAndAuthorityId(userId, authority.getAuthorityId())
                                    .defaultIfEmpty(new UserAuthority(userId, authority.getAuthorityId(), userAuthorities.isActive()))
                                    .flatMap(exitingUser -> {
                                        exitingUser.setActive(userAuthorities.isActive());
                                        return userAuthorityRespository.save(exitingUser);
                                    })
                                    .flatMap(savedUser -> getUserById(savedUser.getUserId())));
            }
        })
        .onErrorResume(DatabaseErrorUtil::handleError);
    }
}
