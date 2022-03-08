package com.example.Forum.Exception;

public class UserCanNotBeActivationException  extends RuntimeException{

    private String errorMessage;

    public UserCanNotBeActivationException(String message)
    {
        super(message);
    }


}

