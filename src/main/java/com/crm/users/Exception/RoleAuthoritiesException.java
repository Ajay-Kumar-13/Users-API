package com.crm.users.Exception;

public class RoleAuthoritiesException extends RuntimeException{
    private final Exception exceptionType;

    public RoleAuthoritiesException(Exception error, Throwable cause) {
        super(error.name(), cause);
        this.exceptionType = error;
    }

    public Exception getExceptionType() {
        return exceptionType;
    }
}
