package com.crm.users.Exception;

import com.crm.users.DTO.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//    These exception handlers will get triggered when the exception are not handled in the reactive pipelines, if handled then these exceptions will get ignored.

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), e.getCause().getMessage()));
    }

    @ExceptionHandler(java.lang.Exception.class)
    public ResponseEntity<?> internalServerError(java.lang.Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(e.getMessage(), e.getCause().getMessage()));
    }

    @ExceptionHandler(UsersException.class)
    public ResponseEntity<?> usersException(java.lang.Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(e.getMessage(), e.getCause().getMessage()));
    }

    @ExceptionHandler(RolesException.class)
    public ResponseEntity<?> rolesException(java.lang.Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(e.getMessage(), e.getCause().getMessage()));
    }

    @ExceptionHandler(UserAuthoritiesException.class)
    public ResponseEntity<?> userAuthoritiesException(java.lang.Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(e.getMessage(), e.getCause().getMessage()));
    }
}
