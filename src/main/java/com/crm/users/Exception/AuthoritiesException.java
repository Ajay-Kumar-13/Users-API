package com.crm.users.Exception;

public class AuthoritiesException extends RuntimeException{
    public AuthoritiesException(Exception message, Throwable cause){
        super(message.name(), cause);
    }
}
