package com.crm.users.Exception;

public class RoleAuthoritiesException extends RuntimeException{
    public RoleAuthoritiesException(Exception error, Throwable cause) {
        super(error.name(), cause);
    }
}
