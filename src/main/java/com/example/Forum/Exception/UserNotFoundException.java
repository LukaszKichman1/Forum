package com.example.Forum.Exception;

public class UserNotFoundException extends RuntimeException {

    private String errorMessage;

    public UserNotFoundException(String message) {
        super(message);
    }
}
