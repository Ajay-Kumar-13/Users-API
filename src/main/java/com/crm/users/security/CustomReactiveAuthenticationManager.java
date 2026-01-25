package com.crm.users.security;

import com.crm.users.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import io.jsonwebtoken.Claims;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    JwtUtils jwtUtils;

    //    This method is used to validate the jwt token.
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        Claims claims = jwtUtils.getClaims(token);
        String role = claims.get("roles").toString();
        List<String> authorities = claims.get("authorities", List.class);
        List<SimpleGrantedAuthority> finalAuthorities = new ArrayList<>();
        if(role != null) {
            finalAuthorities.add(new SimpleGrantedAuthority("ROLE_"+role));
        }
        if(authorities != null) {
            finalAuthorities.addAll(
              authorities.stream().map(SimpleGrantedAuthority::new).toList()
            );
        }
        return Mono.just(new UsernamePasswordAuthenticationToken(claims.getSubject(), null, finalAuthorities));
    }
}
