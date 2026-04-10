package com.crm.users.Exception;

public class UserAuthoritiesException extends RuntimeException{
    private final Exception exceptionType;

    public UserAuthoritiesException(Exception message, Throwable cause) {
        super(message.name(), cause);
        this.exceptionType = message;
    }

    public Exception getExceptionType() {
        return exceptionType;
    }
}
