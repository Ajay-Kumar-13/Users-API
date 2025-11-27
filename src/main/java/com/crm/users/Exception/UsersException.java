package com.crm.users.Exception;

public class UsersException extends RuntimeException{
    public UsersException(Exception message, Throwable cause){
        super(message.name(), cause);
    }
}
