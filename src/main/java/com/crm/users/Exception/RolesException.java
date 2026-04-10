package com.crm.users.Exception;

public class RolesException extends RuntimeException{
    private final Exception exceptionType;

    public RolesException(Exception message, Throwable cause){
        super(message.name(), cause);
        this.exceptionType = message;
    }

    public Exception getExceptionType() {
        return exceptionType;
    }
}
