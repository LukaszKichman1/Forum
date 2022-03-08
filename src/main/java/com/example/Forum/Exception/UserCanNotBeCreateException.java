package com.example.Forum.Exception;

public class UserCanNotBeCreateException extends RuntimeException{

    private String errorMessage;

    public UserCanNotBeCreateException(String message)
    {
        super(message);
    }


}