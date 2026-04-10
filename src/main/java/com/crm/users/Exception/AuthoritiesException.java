package com.crm.users.Exception;

public class AuthoritiesException extends RuntimeException{
    private final Exception exceptionType;

    public AuthoritiesException(Exception message, Throwable cause){
        super(message.name(), cause);
        this.exceptionType = message;
    }

    public Exception getExceptionType() {
        return exceptionType;
    }
}
