package com.crm.users.service;

import com.crm.users.DTO.CreateAuthorityRequest;
import com.crm.users.model.Authorities;
import com.crm.users.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AuthoritiesService {
    @Autowired
    AuthorityRepository authorityRepository;

    public Flux<Authorities> getAllAuthorities() {
        return authorityRepository.findAll();
    }

    public Mono<Authorities> createAuthority(CreateAuthorityRequest authority) {
        Authorities newAuthorities = new Authorities();
        newAuthorities.setAuthority_name(authority.getAuthority_name());
        return authorityRepository.save(newAuthorities);
    }
}
