package com.crm.users.service;

import com.crm.users.DTO.CreateAuthorityRequest;
import com.crm.users.DTO.CreateAuthorityResponse;
import com.crm.users.Exception.AuthoritiesException;
import com.crm.users.Exception.Exception;
import com.crm.users.Exception.RoleAuthoritiesException;
import com.crm.users.Exception.RolesException;
import com.crm.users.model.Authorities;
import com.crm.users.model.Authority;
import com.crm.users.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AuthoritiesService {
    @Autowired
    AuthorityRepository authorityRepository;

    public Flux<CreateAuthorityResponse> getAllAuthorities() {
        return authorityRepository.findAll()
                .map(authority -> new CreateAuthorityResponse(authority.getAuthorityId(), authority.getAuthorityName().name()))
                .onErrorResume(err -> Mono.error(new RoleAuthoritiesException(Exception.ROLE_AUTHORITIES_EXCEPTION, err)));
    }

    public Mono<CreateAuthorityResponse> createAuthority(CreateAuthorityRequest authority) {
        if(authority.getAuthorityName().isBlank()) {
            return Mono.error(new AuthoritiesException(Exception.INVALID_ARGUMENTS, new Error("Invalid Arguments received when creating an authority")));
        }
        Authorities newAuthorities = new Authorities();
        newAuthorities.setAuthorityName(Authority.valueOf(authority.getAuthorityName()));
        return authorityRepository.save(newAuthorities)
                .flatMap(savedAuthority ->
                        Mono.just(new CreateAuthorityResponse(savedAuthority.getAuthorityId(), savedAuthority.getAuthorityName().name())))
                .onErrorResume(err -> Mono.error(new RoleAuthoritiesException(Exception.ROLE_AUTHORITIES_EXCEPTION, err)));
    }
}
