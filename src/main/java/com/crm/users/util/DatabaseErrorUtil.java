package com.crm.users.util;

import com.crm.users.Exception.*;
import com.crm.users.Exception.Exception;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class DatabaseErrorUtil {
    public static <T> Mono<T> handleError(Throwable err) {
        if (err instanceof RolesException){
            return Mono.error(new RolesException(Exception.ROLES_EXCEPTION, err));
        } else if (err instanceof UsersException) {
            return Mono.error(new UsersException(Exception.USERS_EXCEPTION, err));
        } else if (err instanceof AuthoritiesException) {
            return Mono.error(new AuthoritiesException(Exception.AUTHORITIES_EXCEPTION, err));
        } else if (err instanceof UserAuthoritiesException) {
            return Mono.error(new UserAuthoritiesException(Exception.USER_AUTHORITIES_EXCEPTION, err));
        } else if (err instanceof RoleAuthoritiesException) {
            return Mono.error(new RoleAuthoritiesException(Exception.ROLE_AUTHORITIES_EXCEPTION, err));
        }
        return Mono.error(err);
    }
}
