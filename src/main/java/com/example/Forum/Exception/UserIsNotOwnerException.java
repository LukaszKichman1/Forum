package com.example.Forum.Exception;

public class UserIsNotOwnerException extends RuntimeException{

    private String errorMessage;

    public UserIsNotOwnerException(String message)
    {
        super(message);
    }


}