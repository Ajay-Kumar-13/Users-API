package com.crm.users.util;

import com.crm.users.Exception.*;
import com.crm.users.Exception.Exception;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import reactor.core.publisher.Mono;

/**
 * Utility class for handling database errors in reactive pipelines.
 * Transforms generic database exceptions into domain-specific exceptions with proper logging.
 */
@Slf4j
@AllArgsConstructor
public class DatabaseErrorUtil {
    
    public static <T> Mono<T> handleError(Throwable err) {
        if (err instanceof RolesException) {
            log.error("Roles exception caught: {}", err.getMessage(), err);
            return Mono.error(new RolesException(Exception.ROLES_EXCEPTION, err));
        } else if (err instanceof UsersException) {
            log.error("Users exception caught: {}", err.getMessage(), err);
            return Mono.error(new UsersException(Exception.USERS_EXCEPTION, err));
        } else if (err instanceof AuthoritiesException) {
            log.error("Authorities exception caught: {}", err.getMessage(), err);
            return Mono.error(new AuthoritiesException(Exception.AUTHORITIES_EXCEPTION, err));
        } else if (err instanceof UserAuthoritiesException) {
            log.error("User authorities exception caught: {}", err.getMessage(), err);
            return Mono.error(new UserAuthoritiesException(Exception.USER_AUTHORITIES_EXCEPTION, err));
        } else if (err instanceof RoleAuthoritiesException) {
            log.error("Role authorities exception caught: {}", err.getMessage(), err);
            return Mono.error(new RoleAuthoritiesException(Exception.ROLE_AUTHORITIES_EXCEPTION, err));
        } else if (err instanceof DataIntegrityViolationException) {
            log.error("Database constraint violation: {}", err.getMessage(), err);
            return Mono.error(new RuntimeException("Data integrity constraint violated. Please check your input.", err));
        } else if (err instanceof EmptyResultDataAccessException) {
            log.warn("Resource not found in database: {}", err.getMessage());
            return Mono.error(new RuntimeException("Requested resource not found.", err));
        } else if (err instanceof DataAccessException) {
            log.error("Database access error: {}", err.getMessage(), err);
            return Mono.error(new RuntimeException("Database operation failed. Please try again.", err));
        }
        
        log.error("Unhandled error: {}", err.getMessage(), err);
        return Mono.error(err);
    }
}
