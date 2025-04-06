package com.userManagement.UserService.ExceptionHandler;

public class ResourceNotFoundException extends Exception{
    public ResourceNotFoundException(String message){
        super(message);
    }
}
