package com.crm.users.Exception;

import com.crm.users.DTO.ErrorResponse;
import com.crm.users.DTO.FieldError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle validation errors from @Valid annotation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        
        List<FieldError> fieldErrors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.add(new FieldError(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue()
                ))
        );
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Invalid input parameters")
                .code("VALIDATION_ERROR")
                .path(request.getDescription(false).replace("uri=", ""))
                .fieldErrors(fieldErrors)
                .build();
        
        log.warn("Validation error: {}", errorResponse.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException e,
            WebRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid Argument")
                .message(e.getMessage() != null ? e.getMessage() : "Invalid input provided")
                .code("INVALID_ARGUMENT")
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        
        log.warn("Illegal argument error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle UsersException
     */
    @ExceptionHandler(UsersException.class)
    public ResponseEntity<ErrorResponse> handleUsersException(
            UsersException e,
            WebRequest request) {
        
        int status = getStatusCodeForException(e.getExceptionType());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(e.getExceptionType().name())
                .message(e.getMessage() != null ? e.getMessage() : "User operation failed")
                .code("USERS_ERROR")
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        
        log.error("Users exception: {}", e.getMessage(), e);
        return ResponseEntity.status(status).body(errorResponse);
    }

    /**
     * Handle RolesException
     */
    @ExceptionHandler(RolesException.class)
    public ResponseEntity<ErrorResponse> handleRolesException(
            RolesException e,
            WebRequest request) {
        
        int status = getStatusCodeForException(e.getExceptionType());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(e.getExceptionType().name())
                .message(e.getMessage() != null ? e.getMessage() : "Role operation failed")
                .code("ROLES_ERROR")
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        
        log.error("Roles exception: {}", e.getMessage(), e);
        return ResponseEntity.status(status).body(errorResponse);
    }

    /**
     * Handle AuthoritiesException
     */
    @ExceptionHandler(AuthoritiesException.class)
    public ResponseEntity<ErrorResponse> handleAuthoritiesException(
            AuthoritiesException e,
            WebRequest request) {
        
        int status = getStatusCodeForException(e.getExceptionType());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(e.getExceptionType().name())
                .message(e.getMessage() != null ? e.getMessage() : "Authority operation failed")
                .code("AUTHORITIES_ERROR")
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        
        log.error("Authorities exception: {}", e.getMessage(), e);
        return ResponseEntity.status(status).body(errorResponse);
    }

    /**
     * Handle UserAuthoritiesException
     */
    @ExceptionHandler(UserAuthoritiesException.class)
    public ResponseEntity<ErrorResponse> handleUserAuthoritiesException(
            UserAuthoritiesException e,
            WebRequest request) {
        
        int status = getStatusCodeForException(e.getExceptionType());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(e.getExceptionType().name())
                .message(e.getMessage() != null ? e.getMessage() : "User authority operation failed")
                .code("USER_AUTHORITIES_ERROR")
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        
        log.error("User authorities exception: {}", e.getMessage(), e);
        return ResponseEntity.status(status).body(errorResponse);
    }

    /**
     * Handle RoleAuthoritiesException
     */
    @ExceptionHandler(RoleAuthoritiesException.class)
    public ResponseEntity<ErrorResponse> handleRoleAuthoritiesException(
            RoleAuthoritiesException e,
            WebRequest request) {
        
        int status = getStatusCodeForException(e.getExceptionType());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(e.getExceptionType().name())
                .message(e.getMessage() != null ? e.getMessage() : "Role authority operation failed")
                .code("ROLE_AUTHORITIES_ERROR")
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        
        log.error("Role authorities exception: {}", e.getMessage(), e);
        return ResponseEntity.status(status).body(errorResponse);
    }

    /**
     * Handle generic Exception (catch-all)
     */
    @ExceptionHandler(java.lang.Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            java.lang.Exception e,
            WebRequest request) {
        
        String message = e.getMessage() != null ? e.getMessage() : "An internal server error occurred";
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message(message)
                .code("INTERNAL_SERVER_ERROR")
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        
        log.error("Internal server error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Determine HTTP status code based on exception type
     */
    private int getStatusCodeForException(Exception exceptionType) {
        switch (exceptionType) {
            case INVALID_ARGUMENTS:
                return HttpStatus.BAD_REQUEST.value();
            case AUTHENTICATION_FAILED:
                return HttpStatus.UNAUTHORIZED.value();
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
    }
}
