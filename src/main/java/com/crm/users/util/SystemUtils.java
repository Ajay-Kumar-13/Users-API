package com.crm.users.util;

import com.crm.users.DTO.KeyValuePair;
import com.crm.users.model.Role;
import com.crm.users.model.RoleAuthorities;
import com.crm.users.repository.AuthorityRepository;
import com.crm.users.repository.RoleAuthoritiesRepository;
import com.crm.users.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SystemUtils {
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final RoleAuthoritiesRepository roleAuthoritiesRepository;

    public Mono<Role> fetchRole(UUID roleId) {
        return roleRepository.findByRoleId(roleId)
                .onErrorResume(DatabaseErrorUtil::handleError);
    }

    private Flux<RoleAuthorities> fetchRoleAuthorities(UUID roleId) {
        return roleAuthoritiesRepository.findAllByRid(roleId)
                .onErrorResume(DatabaseErrorUtil::handleError);
    }

    public Mono<List<KeyValuePair>> fetchAuthorities(UUID roleId) {
        return fetchRoleAuthorities(roleId).flatMap(roleAuthority ->
                        authorityRepository.findById(roleAuthority.getAid()).map(authority ->
                                new KeyValuePair(authority.getAuthorityId(), authority.getAuthorityName().name())))
                .collectList()
                .onErrorResume(DatabaseErrorUtil::handleError);
    }
}
