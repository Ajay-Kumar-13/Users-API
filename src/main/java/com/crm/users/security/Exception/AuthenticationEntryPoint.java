package com.crm.users.security.Exception;

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

//No JWT token this wll get triggered
//Token parsing failure

@Component
public class AuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        ErrorResponse errorResponse = new ErrorResponse(Exception.AUTHENTICATION_FAILED.name(), "Invalid or expired Access Token");

        return Mono.fromCallable(() -> objectMapper.writeValueAsBytes(errorResponse)).flatMap(
                bytes -> {
                    DataBuffer dataBuffer = exchange.getResponse().bufferFactory().wrap(bytes);
                    return exchange.getResponse().writeWith(Mono.just(dataBuffer));
                }
        );
    }
}
