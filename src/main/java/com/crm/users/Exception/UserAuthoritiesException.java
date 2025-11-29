package com.crm.users.Exception;

public class UserAuthoritiesException extends RuntimeException{
    public UserAuthoritiesException(Exception message, Throwable cause) {
        super(message.name(), cause);
    }
}
