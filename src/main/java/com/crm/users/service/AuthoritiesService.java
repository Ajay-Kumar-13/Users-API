package com.crm.users.service;

import com.crm.users.DTO.CreateAuthorityRequest;
import com.crm.users.DTO.CreateAuthorityResponse;
import com.crm.users.Exception.AuthoritiesException;
import com.crm.users.Exception.Exception;
import com.crm.users.model.Authorities;
import com.crm.users.model.Authority;
import com.crm.users.repository.AuthorityRepository;
import com.crm.users.util.DatabaseErrorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class AuthoritiesService {
    @Autowired
    AuthorityRepository authorityRepository;

    public Flux<CreateAuthorityResponse> getAllAuthorities() {
        return authorityRepository.findAll()
                .map(authority -> new CreateAuthorityResponse(authority.getAuthorityId(), authority.getAuthorityName().name(), authority.getDescription()))
                .onErrorResume(DatabaseErrorUtil::handleError);
    }

    protected Mono<CreateAuthorityResponse> fetchAuthorityByName(Authority auth) {
        return authorityRepository.findByAuthorityName(auth).map(authorities -> new CreateAuthorityResponse(authorities.getAuthorityId(), authorities.getAuthorityName().name(), authorities.getDescription()))
                .onErrorResume(DatabaseErrorUtil::handleError);

    }

    protected Mono<CreateAuthorityResponse> fetchAuthorityById(UUID authId) {
        return authorityRepository.findById(authId).map(authorities -> new CreateAuthorityResponse(authorities.getAuthorityId(), authorities.getAuthorityName().name(), authorities.getDescription()))
                .onErrorResume(DatabaseErrorUtil::handleError);
    }

    public Mono<CreateAuthorityResponse> createAuthority(CreateAuthorityRequest authority) {
        if(authority.getAuthorityName().isBlank()) {
            return Mono.error(new AuthoritiesException(Exception.INVALID_ARGUMENTS, new Error("Invalid Arguments received when creating an authority")));
        }
        Authorities newAuthorities = new Authorities();
        newAuthorities.setAuthorityName(Authority.valueOf(authority.getAuthorityName()));
        newAuthorities.setDescription(authority.getAuthorityDesc());
        return authorityRepository.save(newAuthorities)
                .flatMap(savedAuthority ->
                        Mono.just(new CreateAuthorityResponse(savedAuthority.getAuthorityId(), savedAuthority.getAuthorityName().name(), savedAuthority.getDescription())))
                .onErrorResume(DatabaseErrorUtil::handleError);
    }

    public Mono<CreateAuthorityResponse> updateAuthority(UUID authId, CreateAuthorityRequest authorityRequest) {
        return authorityRepository.findByAuthorityId(authId)
                .flatMap(authority -> {
                    authority.setAuthorityName(Authority.valueOf(authorityRequest.getAuthorityName()));
                    authority.setDescription(authorityRequest.getAuthorityDesc());
                    return authorityRepository.save(authority);
                })
                .flatMap(authority -> Mono.just(new CreateAuthorityResponse(authority.getAuthorityId(), authority.getAuthorityName().name(), authority.getDescription())));
    }

    public Mono<ResponseEntity<String>> deleteAuthority(UUID authorityId) {
        return authorityRepository.deleteByAuthorityId(authorityId)
                .then(Mono.just(ResponseEntity.ok().body("Deleted Successfully!")));
    }
}
