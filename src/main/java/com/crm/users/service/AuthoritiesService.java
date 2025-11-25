package com.crm.users.service;

import com.crm.users.DTO.AuthorityDTO;
import com.crm.users.model.authorities;
import com.crm.users.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AuthoritiesService {
    @Autowired
    AuthorityRepository authorityRepository;

    public Flux<authorities> getAllAuthorities() {
        return authorityRepository.findAll();
    }

    public Mono<authorities> createAuthority(AuthorityDTO authority) {
        authorities newAuthorities = new authorities();
        newAuthorities.setAuthority_name(authority.getAuthority_name());
        return authorityRepository.save(newAuthorities);
    }
}
