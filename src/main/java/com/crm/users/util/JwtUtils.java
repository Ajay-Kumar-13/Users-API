package com.crm.users.util;

import com.crm.users.DTO.CreateUserResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpiration}")
    private String jwtExpiration;

    public Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public Claims getClaims(String token) {
        return Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token).getPayload();
    }

    public String generateJwtFromUsername(CreateUserResponse user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("roles", user.getRole().getName())
                .claim("authorities", user.getAuthorities().stream().map(authority -> authority.getName()).toList())
                .expiration(new Date(System.currentTimeMillis() + Long.parseLong(jwtExpiration)))
                .signWith(key())
                .compact();
    }
}
