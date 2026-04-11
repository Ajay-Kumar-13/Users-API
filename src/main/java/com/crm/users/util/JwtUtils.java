package com.crm.users.util;

import com.crm.users.DTO.CreateUserResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.accessTokenExpiration}")
    private String accessTokenExpiration;

    public Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException("INVALID ACCESS TOKEN");
        } catch (JwtException e) {
             throw new BadCredentialsException("INVALID ACCESS TOKEN");
        }

    }

    public String generateJwtFromUsername(CreateUserResponse user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("id", user.getId())
                .claim("roles", user.getRole().getName())
                .claim("authorities", user.getAuthorities().stream().map(authority -> authority.getName()).toList())
                .expiration(new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpiration)))
                .signWith(key())
                .compact();
    }
}
