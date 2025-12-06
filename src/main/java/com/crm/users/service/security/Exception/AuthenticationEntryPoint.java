package com.crm.users.service.security.Exception;

import com.crm.users.DTO.ErrorResponse;
import com.crm.users.Exception.Exception;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        ErrorResponse errorResponse = new ErrorResponse(Exception.INVALID_ARGUMENTS.name(), ex.getMessage());
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);
            DataBuffer dataBuffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(dataBuffer));
        } catch (java.lang.Exception e){
            throw new RuntimeException(e);
        }
    }
}
