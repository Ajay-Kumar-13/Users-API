package com.crm.users.Exception;

public class RolesException extends RuntimeException{
    public RolesException(Exception message, Throwable cause){
        super(message.name(), cause);
    }
}
