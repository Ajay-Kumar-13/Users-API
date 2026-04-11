package com.crm.users.Exception;

public class UsersException extends RuntimeException{
    private final Exception exceptionType;

    public UsersException(Exception message, Throwable cause){
        super(message.name(), cause);
        this.exceptionType = message;
    }

    public Exception getExceptionType() {
        return exceptionType;
    }
}
